package com.deathsdoor.chillback.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import chillback.`feature-welcome`.generated.resources.Res
import chillback.`feature-welcome`.generated.resources.and
import chillback.`feature-welcome`.generated.resources.download
import chillback.`feature-welcome`.generated.resources.download_sentence1
import chillback.`feature-welcome`.generated.resources.download_sentence2
import chillback.`feature-welcome`.generated.resources.download_sentence2_phrase1
import chillback.`feature-welcome`.generated.resources.download_sentence2_phrase2
import chillback.`feature-welcome`.generated.resources.download_sentence3
import chillback.`feature-welcome`.generated.resources.or
import com.deathsdoor.chillback.core.layout.snackbar.LocalWindowSize
import com.deathsdoor.chillback.core.layout.extensions.asAnnotatedStringWith
import com.deathsdoor.chillback.core.layout.extensions.asAnnotatedStringWithSpanStyle
import com.deathsdoor.chillback.welcome.components.icons.Android
import com.deathsdoor.chillback.welcome.components.icons.Apple
import com.deathsdoor.chillback.welcome.components.icons.Linux
import com.deathsdoor.chillback.welcome.components.icons.Windows
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun DownloadApplication() = Box(modifier = Modifier.fillMaxWidth()) {
    Column(modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(Res.string.download),
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.displayMedium,
        )

        val annotatedString  = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(Res.string.download_sentence1))
            }

            val and = stringResource(Res.string.and)
            val or = stringResource(Res.string.or)

            val allPlatforms = "Linux, Windows , Android $and iOS"
            val phrase1 = stringResource(Res.string.download_sentence2_phrase1,allPlatforms)
                .asAnnotatedStringWithSpanStyle(
                    substring = allPlatforms,
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                )

            val mobileDevices = "Android $or iOS"
            val phrase2 = stringResource(Res.string.download_sentence2_phrase2,mobileDevices)
                .asAnnotatedStringWithSpanStyle(
                    substring = mobileDevices,
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                )

            append(
                phrase1,
                phrase2,
                stringResource(Res.string.download_sentence2)
            )

            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                val readme = "README."
                stringResource(Res.string.download_sentence3,readme).asAnnotatedStringWith(readme) { section ->
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                        pushStringAnnotation(tag = README_TAG, annotation = "https://github.com/Deaths-Door/Chillback/blob/main/INSTALLATION_INSTRUCTIONS.md")
                        append(readme)
                        pop()
                    }
                }
            }
        }

        val uriHandler = LocalUriHandler.current

        ClickableText(
            modifier = Modifier.padding(top = 16.dp,bottom = 24.dp),
            text = annotatedString,
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.inverseSurface),
            onClick = { offset ->
                annotatedString
                    .getStringAnnotations(tag = README_TAG,start = offset,end = offset)
                    .firstOrNull()?.let {
                        uriHandler.openUri(it.item)
                    }
            },
        )


        val windowSize= LocalWindowSize.current

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            if(windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                WindowsButton(uriHandler = uriHandler)
                LinuxButton(uriHandler = uriHandler)
                AndroidButton(uriHandler = uriHandler)
                iOSButton(uriHandler = uriHandler)
            } else ApplicationDownloadForPlatform()
        }
    }
}

private val README_TAG = "readme"

@Composable
internal fun ApplicationDownloadForPlatform() {
    val uriHandler = LocalUriHandler.current

    when(userPlatform()){
        "android" -> AndroidButton(uriHandler = uriHandler)
        "ios" -> iOSButton(uriHandler = uriHandler)
        "windows" -> WindowsButton(uriHandler = uriHandler)
        "linux" -> LinuxButton(uriHandler = uriHandler)
        // TODO : Extract this
        "unknown" , "macos" -> Text(text ="This platform is not support", fontWeight = FontWeight.Bold)
    }
}

internal const val ANDROID_LINK = "https://apps.apple.com/de/app/chillback/"
// TODO : Set correct appstore url
internal const val IOS_LINK = "https://apps.apple.com/de/app/chillback/"

private val buttonIconModifier = Modifier.padding(end = 8.dp)

@Composable
private fun iOSButton(uriHandler: UriHandler) = OutlinedButton(
    content = {
        Icon(modifier = buttonIconModifier,imageVector = Icons.Apple, contentDescription = null)
        Text("iOS")
    },
    onClick = { uriHandler.openUri(IOS_LINK) }
)

@Composable
private fun AndroidButton(uriHandler: UriHandler) = OutlinedButton(
    content = {
        Icon(modifier = buttonIconModifier,imageVector = Icons.Android, contentDescription = null)
        Text("iOS")
    },

    onClick = { uriHandler.openUri(ANDROID_LINK) }
)

// TODO : For Linux and Windows call the server to get the url of the lastest download
@Composable
private fun LinuxButton(uriHandler: UriHandler) =   OutlinedButton(
    content = {
        Icon(modifier = buttonIconModifier,imageVector = Icons.Linux, contentDescription = null)
        Text("Linux")
    },
    onClick = { }
)

@Composable
private fun WindowsButton(uriHandler: UriHandler) = OutlinedButton(
    content = {
        Icon(modifier = buttonIconModifier,imageVector = Icons.Windows,tint = Color.Unspecified, contentDescription = null)
        Text("Windows")
    },
    onClick = { }
)

internal fun userPlatform() : String = js("""
function() {
const ua = navigator.userAgent.toLowerCase();

if (/android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(ua)) {
    return "android"; // Group common mobile platforms
}

if (/iphone|ipad|ipod/i.test(ua)) {
    return "ios";
}

const platform = navigator.platform;

if (/windows phone/i.test(ua) || /windows nt/i.test(ua) || /win/i.test(platform)) {
    return "windows";
}

if (/linux/i.test(platform)) {
    return "linux";
}

if (/mac os x/i.test(ua)) {
    return "macos";
} 

return "unknown";
}()
""")