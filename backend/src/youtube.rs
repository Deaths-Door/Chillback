use std::{error::Error, fs::File, io::Read};

use google_youtube3::{client::remove_json_null_values, hyper, hyper_rustls, oauth2::{ApplicationSecret,InstalledFlowAuthenticator,InstalledFlowReturnMethod}, YouTube};
use rusty_ytdl::{reqwest::Proxy, RequestOptions, Video, VideoOptions, VideoSearchOptions};

#[derive(Debug,thiserror::Error,uniffi::Error)]
pub enum YoutubeError {
    #[error("{}",.0)]
    Download(String),
    #[error("{}",.0)]
    Video(String),
    #[error("{}",.0)]
    Proxy(String),
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
) -> Result<(),YoutubeError> {
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

    let video = Video::new_with_options(video_url, video_options).map_err(|e| YoutubeError::Video(e.to_string()))?;
    
    video.download(output_file).await.map_err(|e| YoutubeError::Download(e.to_string()))
}

// TODO : Get this to work in the future 
pub async fn search_youtube(
    track_name : &str,
    track_artist : &str,
    lyrics: bool,
    // none , strict , moderate
    filter_moderate : Option<bool>
    // TODO : Return smth more useful
) -> Result<(),String>  {
    // Get an ApplicationSecret instance by reading local file
    let mut file_content = String::new();
    
    File::open("youtube_data_api_secret.json")
        .map_err(|e| e.to_string())?
        .read_to_string(&mut file_content)
        .map_err(|e| e.to_string())?;

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

    // You can configure optional parameters by calling the respective setters at will, and
    // execute the final call using `doit()`.
    // Values shown here are possibly random and not representative !
    let lyrics = if lyrics { "lyrics" } else { "" };
    let query = format!("{track_name} by {track_artist} {lyrics}");
    let safe_search = match filter_moderate {
        None => "none",
        Some(true) => "moderate",
        Some(false) => "strict"
    };
    
    let result = hub.search()
        .list(&vec!["snippet".to_string()])
        .add_scope("https://www.googleapis.com/auth/youtube.readonly")
        .max_results(5)
        .safe_search(safe_search)
        .q(&query)
        .add_type("video")
        .doit()
        .await;

    let (response ,output_schema) = result.unwrap();

    
    let mut value = serde_json::value::to_value(&output_schema).expect("serde to work");
    remove_json_null_values(&mut value);

    println!("{:?} \n---------\n {:?}",response,output_schema);

    Ok(())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[tokio::test]
    async fn search_videos() {
        search_youtube(
            "let her go",
            "passenger",
            false,
            None
        ).await.unwrap()
    } 
}