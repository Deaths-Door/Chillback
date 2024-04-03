use std::{fs::File, io::Read};

use google_youtube3::{hyper, hyper_rustls, oauth2::{ApplicationSecret,InstalledFlowAuthenticator,InstalledFlowReturnMethod}, YouTube};
//use r2d2::PooledConnection;
//use r2d2_sqlite::SqliteConnectionManager;
//use rusqlite::Connection;
use rusty_ytdl::{reqwest::Proxy, RequestOptions, Video, VideoOptions, VideoSearchOptions};

//use crate::database::cache_instance;


type YoutubeResult<T> = Result<T,YoutubeError>;

#[derive(Debug,thiserror::Error,uniffi::Error)]
pub enum YoutubeError {
    #[error("{}",.0)]
    Download(String),
    #[error("{}",.0)]
    Video(String),
    #[error("{}",.0)]
    Proxy(String),
    #[error("{}",.0)]
    File(String),
}

/// Downloads a YouTube video and saves it to a file.
/// # Arguments
/// * `video_url`: A string slice containing the URL of the YouTube video to download.
/// * `output_file`: A string slice containing the path and filename where the downloaded video will be saved.
/// * `scheme` (Optional): An `Option<String>` specifying the proxy scheme to use for the download. If `None`, no proxy will be used.
/// * `authentication` (Optional): An `Option<(String, String)>` containing a username and password for proxy authentication. This is only used if a proxy scheme is specified.
#[uniffi::export]
pub async fn download_youtube_video(
    video_url : &str,
    output_file : &str,
    scheme : Option<String>,
    // Username Password
    username : Option<String>,
    password : Option<String>
) -> YoutubeResult<()> {
    let video_options = VideoOptions {
        quality : rusty_ytdl::VideoQuality::HighestAudio,
        filter : VideoSearchOptions::Audio,
        request_options : RequestOptions {
            proxy : match scheme {
                None => None,
                Some(scheme) => {
                    let mut proxy = Proxy::https(scheme).map_err(|e| YoutubeError::Proxy(e.to_string()))?;
                    
                    if let Some(username) = username {
                        if let Some(password) = password {
                            proxy = proxy.basic_auth(&username, &password)
                        }
                    }
        
                    Some(proxy)
                }
            },
            ..Default::default()
        },
        ..Default::default()
    };

    // TODO : Download audio instead of video
    let video = Video::new_with_options(video_url, video_options).map_err(|e| YoutubeError::Video(e.to_string()))?;
    
    video.download(output_file).await.map_err(|e| YoutubeError::Download(e.to_string()))
}

#[cfg(test)]
mod tests {
    use super::*;

    #[tokio::test]
    async fn download_mp3() {
        super::download_youtube_video("https://www.youtube.com/shorts/pmcTmWO7ZOQ", "test.mp4", None, None, None).await.expect("Failure");
        super::download_youtube_video("https://www.youtube.com/shorts/pmcTmWO7ZOQ", "test.mp3", None, None, None).await.expect("Failure");

    }
}

/*

/// Searches for a YouTube video based on provided criteria and returns its `videoId` to pass as an argument to [download_youtube_video]
/// # Arguments
/// * `track_name`: A string slice containing the name of the track to search for.
/// * `track_artist`: A string slice containing the artist name (optional).
/// * `lyrics`: A boolean indicating whether to use lyrics (if available) to refine the search (optional).
/// * `filter_moderate`: An `Option<bool>` specifying the search filter strictness.
///     * `None`: No specific filter applied.
///     * `Some(false)`: Strict filter (might return fewer results).
///     * `Some(true)`: Moderate filter (might return more irrelevant results).
/// * `output_file`: A string slice containing the path and filename where the downloaded audio will be saved.
/// * `scheme` (Optional): An `Option<String>` specifying the proxy scheme to use for the download. If `None`, no proxy will be used.
/// * `username` (Optional): An `Option<String>` containing a username for proxy authentication. This is only used if a proxy scheme is specified.
/// * `password` (Optional): An `Option<String>` containing a password for proxy authentication. This is only used if a proxy scheme is specified.
#[uniffi::export]
pub async fn search_and_download_youtube(
    track_name : &str,
    track_artist : &str,
    lyrics: bool,
    // none , strict , moderate
    filter_moderate : Option<bool>,
    output_file : &str,
    scheme : Option<String>,
    // Username Password
    username : Option<String>,
    password : Option<String>
) -> YoutubeResult<String>  {
    const LYRICS : &str = "lyrics";
    let lyrics_str = if lyrics { LYRICS } else { "" };
    let query = format!("{track_name} by {track_artist} {lyrics_str}");
    let safe_search = match filter_moderate {
        None => "none",
        Some(true) => "moderate",
        Some(false) => "strict"
    };

    create_table();

    // Get an ApplicationSecret instance by reading local file
    let mut file_content = String::new();
    
    // TODO : In the future, create a free and paid tier, or ask users for credientals instead of making a paid tier 
    // so that in the free tier , every user has eg 5 calls per day and then they need to wait , or explore other options 
    // provided. 
    File::open("youtube_data_api_secret.json")
        .map_err(|e| YoutubeError::File(e.to_string()))?
        .read_to_string(&mut file_content)
        .map_err(|e| YoutubeError::File(e.to_string()))?;

    let secret = serde_json::from_str::<ApplicationSecret>(&file_content).unwrap();
    
    // Instantiate the authenticator. It will choose a suitable authentication flow for you, 
    // unless you replace  `None` with the desired Flow.
    // Provide your own `AuthenticatorDelegate` to adjust the way it operates and get feedback about 
    // what's going on. You probably want to bring in your own `TokenStorage` to persist tokens and
    // retrieve them from storage.
    let auth = InstalledFlowAuthenticator::builder(
            secret,
            InstalledFlowReturnMethod::HTTPRedirect,
        ).build().await.unwrap();

    let connector = hyper_rustls::HttpsConnectorBuilder::new()
        .with_native_roots()
        .https_or_http()
        .enable_http1()
        .build();

    let client = hyper::Client::builder().build(connector);
    let hub = YouTube::new(client, auth);
    
    let result = hub.search()
        .list(&vec!["snippet".to_string()])
        .add_scope("https://www.googleapis.com/auth/youtube.readonly")
        .max_results(5)
        .safe_search(safe_search)
        .q(&query)
        .add_type("video")
        .doit()
        .await;

    // We do not need output_schema for now
    let (response ,_output_schema) = result.unwrap();

    let json = hyper::body::to_bytes(response.into_body()).await.map_err(|e| YoutubeError::Video(e.to_string()))?;
    let value = serde_json::value::to_value(&*json).unwrap();
    let snippets = value["items"].as_array().unwrap();

    if snippets.is_empty() {
        return Err(YoutubeError::Video("Search Query returned no results".into()))
    }

    let score : fn(&str,&str,&str,&str,&str) -> u8 = match lyrics {
        true => |title : &str,description : &str,channel : &str,track_artist : &str,_ : &str| -> u8 {
            title.contains(LYRICS) as u8 +
            description.contains(LYRICS) as u8 + 
            channel.contains(track_artist) as u8 
        },
        false => |title : &str,description : &str,channel : &str,track_artist : &str,track_name : &str| -> u8 {
            title.contains(track_name) as u8 + 
            title.contains(track_artist) as u8 + 
            description.contains(track_name) as u8 + 
            description.contains(track_artist) as u8 +
            channel.contains(track_name) as u8 + 
            channel.contains(track_artist) as u8
        }
    };
     
    let track_artist = &track_artist.to_lowercase();
    let track_name = &track_name.to_lowercase();
    
    let (_,video_id) = snippets.iter()
        .map(|item| {
            let snippet = &value["snippet"];
            let title = &snippet["title"].as_str().unwrap();
            let description = &snippet["description"].as_str().unwrap();
            let channel = &snippet["channelTitle"].as_str().unwrap();
            let id = &item["videoId"];

            let score = score(title,description,channel,track_artist,track_name);
            (score,id)
        })
        .max_by(|x,y| x.0.cmp(&y.0))
        // As we return early if snippets are empty
        .unwrap();
    

    cache_search_results(&query, filter_moderate, video_id);

    Ok(video_id.as_str().unwrap().to_string())
}


fn create_table() -> Result<(),()> {
    if let Ok(cache) = cache_instance().lock() {
        if let Ok(instance) = cache.get() {
            instance.execute(
                "CREATE TABLE IF NOT EXISTS youtube_cache ( video_id INTEGER,filter_moderate BIT,query TEXT NOT NULL PRIMARY KEY )"
                , [])?;

        }
    };

    Err(())
}

fn cache_search_results(query : &str,filter_moderate : Option<bool>,video_id : &str) {
    cache_instance().lock()
        .unwrap()
        .get()
        .unwrap()
        .prepare_cached(sql);
}
#[cfg(test)]
mod tests {
    use super::*;

    #[tokio::test]
    async fn search_videos() {
        search_and_download_youtube(
            "let her go",
            "passenger",
            false,
            None,
            "test.mp4",
            None,
            None,
            None
        ).await.expect("Failure");
    } 
}*/