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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier.statusBarsPadding().navigationBarsPadding()
            .fillMaxSize()
            .background(color = Color(0xFFF6F6F6).copy(alpha = .2f))
            .padding(horizontal = 16.dp)
    )
    {

        LanguageTopBar(
            onNextClicked = { onNextClicked(selectedLanguage) },
            isNextEnabled = true
        )


        LanguageSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredLanguages.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
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
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "App Language",
        )

        Button(
            onClick = onNextClicked,
            enabled = isNextEnabled,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4268FF),
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray.copy(alpha = 0.5f),
                disabledContentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Next", fontWeight = FontWeight.Bold, color = Color(0xFFFFFFFF))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
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
            .fillMaxWidth(),
        placeholder = { Text("Search Language", color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.3f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.3f),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(color = MaterialTheme.colorScheme.surfaceBright)
    )
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onSelect: (Language) -> Unit
) {
    //val borderColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    val borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = .2f)
    //val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.2f)
    val backgroundColor = MaterialTheme.colorScheme.background

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onSelect(language) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
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
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = { onSelect(language) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF4267B2),
                unselectedColor = Color.LightGray
            )
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No language found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Try a different spelling",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageScreenPreview() {
    QrCodeAppTheme(darkTheme = false) {
        LanguageScreen()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LanguageScreenDarkPreview() {
    QrCodeAppTheme(darkTheme = true) {
        LanguageScreen()
    }
}
