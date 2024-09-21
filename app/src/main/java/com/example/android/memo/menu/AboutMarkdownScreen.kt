package com.example.android.memo.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.android.memo.R
import com.example.android.memo.util.MenuAppBar
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun AboutMarkdownScreen(
    @StringRes menuBarTitle: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MenuAppBar(
                title = menuBarTitle,
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        AboutMarkdownContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun AboutMarkdownContent(modifier: Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            MarkdownText("""
                # Markdown
                Based on [this cheatsheet][cheatsheet]

                ---

                ## Headers
                ---
                # Header 1
                ## Header 2
                ### Header 3
                #### Header 4
                ##### Header 5
                ###### Header 6
                ---

                ## Emphasis

                Emphasis, aka italics, with *asterisks* or _underscores_.

                Strong emphasis, aka bold, with **asterisks** or __underscores__.

                Combined emphasis with **asterisks and _underscores_**.

                Strikethrough uses two tildes. ~~Scratch this.~~

                ---

                ## Lists
                1. First ordered list item
                2. Another item
                  * Unordered sub-list.
                1. Actual numbers don't matter, just that it's a number
                  1. Ordered sub-list
                4. And another item.

                   You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).

                   To have a line break without a paragraph, you will need to use two trailing spaces.
                   Note that this line is separate, but within the same paragraph.
                   (This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)

                * Unordered list can use asterisks
                - Or minuses
                + Or pluses

                ---

                ## Links

                [I'm an inline-style link](https://www.google.com)

                [I'm a reference-style link][Arbitrary case-insensitive reference text]

                [I'm a relative reference to a repository file](../blob/master/LICENSE)

                [You can use numbers for reference-style link definitions][1]

                Or leave it empty and use the [link text itself].

                ---

                ## Code

                Inline `code` has `back-ticks around` it.

                ```javascript
                var s = "JavaScript syntax highlighting";
                alert(s);
                ```

                ```python
                s = "Python syntax highlighting"
                print s
                ```

                ```java
                /**
                 * Helper method to obtain a Parser with registered strike-through &amp; table extensions
                 * &amp; task lists (added in 1.0.1)
                 *
                 * @return a Parser instance that is supported by this library
                 * @since 1.0.0
                 */
                @NonNull
                public static Parser createParser() {
                  return new Parser.Builder()
                      .extensions(Arrays.asList(
                          StrikethroughExtension.create(),
                          TablesExtension.create(),
                          TaskListExtension.create()
                      ))
                      .build();
                }
                ```

                ```xml
                <ScrollView
                  android:id="@+id/scroll_view"
                  android:layout_width="match_parent"
                  android:layout_height="match_parent"
                  android:layout_marginTop="?android:attr/actionBarSize">

                  <TextView
                    android:id="@+id/text"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_margin="16dip"
                    android:lineSpacingExtra="2dip"
                    android:textSize="16sp"
                    tools:text="yo\nman" />

                </ScrollView>
                ```

                ```
                No language indicated, so no syntax highlighting.
                But let's throw in a <b>tag</b>.
                ```

                ---

                ## Tables

                Colons can be used to align columns.

                | Tables        | Are           | Cool  |
                | ------------- |:-------------:| -----:|
                | col 3 is      | right-aligned | ${'$'}1600 |
                | col 2 is      | centered      |   ${'$'}12 |
                | zebra stripes | are neat      |    ${'$'}1 |

                There must be at least 3 dashes separating each header cell.
                The outer pipes (|) are optional, and you don't need to make the
                raw Markdown line up prettily. You can also use inline Markdown.

                Markdown | Less | Pretty
                --- | --- | ---
                *Still* | `renders` | **nicely**
                1 | 2 | 3

                ---

                ## Blockquotes

                > Blockquotes are very handy in email to emulate reply text.
                > This line is part of the same quote.

                Quote break.

                > This is a very long line that will still be quoted properly when it wraps. Oh boy let's keep writing to make sure this is long enough to actually wrap for everyone. Oh, you can *put* **Markdown** into a blockquote.

                Nested quotes
                > Hello!
                >> And to you!

                ---

                ## Inline HTML

                ```html
                <u><i>H<sup>T<sub>M</sub></sup><b><s>L</s></b></i></u>
                ```

                <u><i>H<sup>T<sub>M</sub></sup><b><s>L</s></b></i></u>

                ---

                ## Horizontal Rule

                Three or more...

                ---

                Hyphens (`-`)

                ***

                Asterisks (`*`)

                ___

                Underscores (`_`)


                ## License

                ```   Copyright [yyyy] [name of copyright owner]
                
                Licensed under the Apache License, Version 2.0 (the "License");
                you may not use this file except in compliance with the License.
                You may obtain a copy of the License at

                  http://www.apache.org/licenses/LICENSE-2.0

                Unless required by applicable law or agreed to in writing, software
                distributed under the License is distributed on an "AS IS" BASIS,
                WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                See the License for the specific language governing permissions and
                limitations under the License.
                ```
            """.trimIndent())
        }
    }
}
