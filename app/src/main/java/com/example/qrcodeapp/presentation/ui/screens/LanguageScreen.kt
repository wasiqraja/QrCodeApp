package com.example.qrcodeapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qrcodeapp.R
import com.example.qrcodeapp.core.utils.AddHeight
import com.example.qrcodeapp.core.utils.AddWidth
import com.example.qrcodeapp.presentation.ui.theme.QrCodeAppTheme
import java.util.Locale

data class Language(
    val name: String,
    val nativeName: String? = null,
    val flagEmoji: String,
    val code: String
)

val languages = listOf(
    Language("English", null, "🇺🇸", "en"),
    Language("Afrikaans", null, "🇿🇦", "af"),
    Language("Albanian", "(shqiptare)", "🇦🇱", "sq"),
    Language("Amharic", null, "🇪🇹", "am"),
    Language("Arabic", "(عربي)", "🇸🇦", "ar"),
    Language("Armenian", null, "🇦🇲", "hy"),
    Language("Assamese", "(অসমীয়া)", "🇮🇳", "as"),
    Language("Aymara", null, "🇧🇴", "ay"),
    Language("Vietnamese", "(Tiếng Việt)", "🇻🇳", "vi")
)

@Composable
fun LanguageScreen(
    onNextClicked: (Language) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf(languages.first()) }

    val filteredLanguages = if (searchQuery.isEmpty()) {
        languages
    } else {
        languages.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    (it.nativeName?.contains(searchQuery, ignoreCase = true) == true)
        }
    }




    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    )
    {

        AddHeight(10)
        LanguageTopBar(
            onNextClicked = { onNextClicked(selectedLanguage) },
            isNextEnabled = true
        )
        AddHeight(25)
        LanguageSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredLanguages.isEmpty()) {
            EmptyLanguageCard( fontFamily = FontFamily(Font(R.font.visbycf_demibold)))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            )
            {
                if (searchQuery.isEmpty()) {
                    item {
                        SectionHeader("Default Language")
                    }
                    item {
                        LanguageItem(
                            language = languages.first(),
                            isSelected = selectedLanguage == languages.first(),
                            onSelect = { selectedLanguage = it }
                        )
                    }
                    item {
                        SectionHeader("All Languages")
                    }
                    items(languages.drop(1)) { language ->
                        LanguageItem(
                            language = language,
                            isSelected = selectedLanguage == language,
                            onSelect = { selectedLanguage = it }
                        )
                    }
                } else {
                    item {
                        SectionHeader(
                            String.format(
                                Locale.getDefault(),
                                "%02d Results",
                                filteredLanguages.size
                            )
                        )
                    }
                    items(filteredLanguages) { language ->
                        LanguageItem(
                            language = language,
                            isSelected = selectedLanguage == language,
                            onSelect = { selectedLanguage = it }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun LanguageTopBar(
    onNextClicked: () -> Unit,
    isNextEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_language),
            fontFamily = FontFamily(Font(R.font.albertsans_semibold)),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 2.dp)
        )


        Button(
            modifier = Modifier.wrapContentWidth(),
            onClick = onNextClicked,
            enabled = isNextEnabled,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4268FF),
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray.copy(alpha = 0.5f),
                disabledContentColor = Color.White
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = "Next",
                    fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 2.dp)
                )
                AddWidth(8)

            }
        }
    }
}

@Composable
fun LanguageSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = "Search Language",
                fontFamily = FontFamily(Font(R.font.visbycf_demibold)),
                color = MaterialTheme.colorScheme.secondary.copy(alpha = .6f),
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 2.dp)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_view_lang_icon),
                contentDescription = null,
                tint = Color.Gray
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .padding(vertical = 8.dp)

    )
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onSelect: (Language) -> Unit
) {
    //val borderColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    val borderColor = MaterialTheme.colorScheme.tertiary
    //val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.2f)
    val backgroundColor = MaterialTheme.colorScheme.background

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onSelect(language) }
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(32.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = language.flagEmoji, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = buildString {
                append(language.name)
                if (language.nativeName != null) {
                    append(" ")
                    append(language.nativeName)
                }
            },
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = { onSelect(language) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF4268FF),
                unselectedColor = Color(0xFF6C727F)
            )
        )
    }
}


@Composable
fun EmptyLanguageCard(
    modifier: Modifier = Modifier,
    fontFamily: FontFamily
) {

    Card(
        modifier = modifier.padding(bottom = 35.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
    )

    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            // Folder Icon
            Icon(
                painter = painterResource(id = R.drawable.empty_search_icon), // Replace with your icon
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFFB0B0B0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = "No Language Found",
                fontFamily = fontFamily,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier= Modifier.padding(horizontal = 6.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-description
            Text(
                text = "Try a different spelling",
                fontFamily = fontFamily,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun LanguageScreenPreview() {
    QrCodeAppTheme(darkTheme = false) {
        LanguageScreen()
    }
}
*/
