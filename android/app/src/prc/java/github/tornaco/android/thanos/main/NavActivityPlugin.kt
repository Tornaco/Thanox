/*
 * (C) Copyright 2022 Thanox
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
 *
 */

package github.tornaco.android.thanos.main

import android.app.Activity
import android.text.Html
import androidx.preference.PreferenceManager
import github.tornaco.android.thanos.BuildProp
import github.tornaco.android.thanos.R
import github.tornaco.android.thanos.widget.ModernAlertDialog
import util.AssetUtils

object NavActivityPlugin {
    private val privacyAgreementKey get() = "PREF_PRIVACY_STATEMENT_ACCEPTED_V_" + BuildProp.THANOS_VERSION_CODE
    private var isPrivacyDialogShowing = false

    fun blockOnCreate(activity: Activity): Boolean {
        return false
    }

    fun showPrivacyAgreement(activity: NavActivity): Boolean {
        if (isPrivacyStatementAccepted(activity)) {
            return false
        }
        if (isPrivacyDialogShowing) {
            return false
        }
        val privacyAgreement =
            AssetUtils.readFileToStringOrThrow(activity, "privacy/privacy_agreement.html")

        val dialog = ModernAlertDialog(activity).apply {
            setDialogTitle(activity.getString(R.string.privacy_agreement_title))
            setDialogMessage(Html.fromHtml(privacyAgreement))
            setCancelable(false)
            setPositive(activity.getString(R.string.privacy_agreement_accept))
            setNegative(activity.getString(R.string.privacy_agreement_discard))
        }
        dialog.setOnNegative {
            activity.finishAffinity()
            isPrivacyDialogShowing = false
        }
        dialog.setOnPositive { setPrivacyStatementAccepted(activity) }
        dialog.show()
        isPrivacyDialogShowing = true

        return true
    }

    private fun isPrivacyStatementAccepted(activity: NavActivity): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(
            privacyAgreementKey, false
        )
    }

    private fun setPrivacyStatementAccepted(activity: NavActivity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit()
            .putBoolean(privacyAgreementKey, true).commit()
    }
}