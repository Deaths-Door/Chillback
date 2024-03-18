package com.deathsdoor.chillback.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.deathsdoor.chillback.R
import com.deathsdoor.chillback.ui.extensions.openLink
import com.deathsdoor.chillback.ui.extensions.tag
import com.deathsdoor.chillback.ui.extensions.themeBasedTint


private fun String.asGithubFile() = "https://github.com/Deaths-Door/Chillback/tree/main/$this"

private fun AnnotatedString.Builder.clickableLink(
    tag : String,
    source : String,
    text : AnnotatedString.Builder.() -> Unit
) {
    pushStringAnnotation(tag = tag, annotation = source)
    text()
    pop()
}

private const val POLICY_TAG = "policy"
private const val TERMS_CONDITION_TAG = "terms_condition"

@Composable
fun TermsAndPolicyDisclaimer(
    modifier : Modifier = Modifier,
    imageModifier : Modifier = Modifier,
    style : TextStyle
) = Row(modifier = modifier) {
    Icon(
        painter= painterResource(id = R.drawable.policy),
        modifier = imageModifier.align(Alignment.CenterVertically),
        tint = themeBasedTint(),
        contentDescription = null
    )

    Column {
        Text(
            text = "Disclaimer",
            fontWeight = FontWeight.Bold,
            style= style
        )

        val primarySpanStyle= SpanStyle(color = MaterialTheme.colorScheme.primary)
        val policyUrl = "PRIVACY.md".asGithubFile()
        val termConditionUrl = "TERM_CONDITION.md".asGithubFile()

        val annotatedString = buildAnnotatedString {
            append("By continuing, you agree to the ")

            clickableLink(
                tag = POLICY_TAG,
                source = policyUrl,
                text = {
                    withStyle(style = primarySpanStyle) {
                        append("Privacy Policy")
                    }
                }
            )

            append(" and ")

            clickableLink(
                tag = TERMS_CONDITION_TAG,
                source = termConditionUrl,
                text = {
                    withStyle(style = primarySpanStyle) {
                        append("Terms & Condition")
                    }
                }
            )
        }

        val uriHandler = LocalUriHandler.current
        ClickableText(text = annotatedString, style = style, onClick = { offset ->
            annotatedString.tag(tag = POLICY_TAG,offset = offset)?.openLink(uriHandler = uriHandler)
            annotatedString.tag(tag = TERMS_CONDITION_TAG,offset = offset)?.openLink(uriHandler = uriHandler)
        })
    }
}