package com.deathsdoor.chillback.feature.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.deathsdoor.chillback.core.layout.extensions.styleWith
import com.deathsdoor.chillback.feature.welcome.extensions.asGithubFileForLegal
import com.deathsdoor.chillback.feature.welcome.extensions.clickableLink
import com.deathsdoor.chillback.feature.welcome.extensions.openLink
import com.deathsdoor.chillback.feature.welcome.extensions.tag
import com.deathsdoor.chillback.feature.welcome.icons.Policy
import com.deathsdoor.chillback.features.welcome.resources.Res
import dev.icerock.moko.resources.compose.stringResource

private const val POLICY_TAG = "policy"
private const val TERMS_CONDITION_TAG = "terms_condition"

@Composable
internal fun TermsAndPolicyDisclaimer(
    modifier : Modifier = Modifier,
    imageModifier : Modifier = Modifier,
    style : TextStyle
) = Row(modifier = modifier) {
    Icon(
        imageVector = Icons.Policy,
        modifier = imageModifier.align(Alignment.CenterVertically),
        contentDescription = null
    )

    Column {
        Text(
            text = stringResource(Res.strings.disclaimer),
            fontWeight = FontWeight.Bold,
            style= style
        )

        val spanStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)
        val annotatedString = Res.strings.term_and_policy_disclaimer.styleWith(
            onAppend = { index , phrase ->
                val (tag,source) = when(index) {
                    0 -> POLICY_TAG to "PRIVACY.md".asGithubFileForLegal()
                    1 -> TERMS_CONDITION_TAG to "TERM_CONDITION.md".asGithubFileForLegal()
                    else -> error("Unreachable")
                }

                clickableLink(tag,source) {
                    withStyle(spanStyle) {
                        append(phrase)
                    }
                }
            },
            Res.strings.term_and_policy_disclaimer_p1,
            Res.strings.term_and_policy_disclaimer_p2
        )

        val uriHandler = LocalUriHandler.current

        ClickableText(text = annotatedString, style = style, onClick = { offset ->
            annotatedString.tag(tag = POLICY_TAG,offset = offset)?.openLink(uriHandler = uriHandler)
            annotatedString.tag(tag = TERMS_CONDITION_TAG,offset = offset)?.openLink(uriHandler = uriHandler)
        })
    }
}