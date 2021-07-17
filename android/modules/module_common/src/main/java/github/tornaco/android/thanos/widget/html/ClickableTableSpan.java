/*
 * Copyright (C) 2016 Richard Thai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.widget.html;

import android.text.style.ClickableSpan;

/**
 * This span defines what should happen if a table is clicked. This abstract class is defined so
 * that applications can access the raw table HTML and do whatever they'd like to render it (e.g.
 * show it in a WebView).
 */
public abstract class ClickableTableSpan extends ClickableSpan {
    protected String tableHtml;

    // This sucks, but we need this so that each table can get its own ClickableTableSpan.
    // Otherwise, we end up removing the clicking from earlier tables.
    public abstract ClickableTableSpan newInstance();

    public void setTableHtml(String tableHtml) {
        this.tableHtml = tableHtml;
    }

    public String getTableHtml() {
        return tableHtml;
    }
}
