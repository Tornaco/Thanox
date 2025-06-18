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

package github.tornaco.thanos.android.module.profile.repo

import androidx.annotation.Keep

/*
* {
        "name": "good_first_profile.json",
        "path": "files/profile/good_first_profile.json",
        "sha": "69609adbfbbb09354a009ca3fe5d934e7f9d1abe",
        "size": 249,
        "url": "https://api.github.com/repos/Tornaco/Thanox/contents/files/profile/good_first_profile.json?ref=master",
        "html_url": "https://github.com/Tornaco/Thanox/blob/master/files/profile/good_first_profile.json",
        "git_url": "https://api.github.com/repos/Tornaco/Thanox/git/blobs/69609adbfbbb09354a009ca3fe5d934e7f9d1abe",
        "download_url": "https://raw.githubusercontent.com/Tornaco/Thanox/master/files/profile/good_first_profile.json",
        "type": "file",
        "_links": {
            "self": "https://api.github.com/repos/Tornaco/Thanox/contents/files/profile/good_first_profile.json?ref=master",
            "git": "https://api.github.com/repos/Tornaco/Thanox/git/blobs/69609adbfbbb09354a009ca3fe5d934e7f9d1abe",
            "html": "https://github.com/Tornaco/Thanox/blob/master/files/profile/good_first_profile.json"
        }
    }
*

* {
  "type": "file",
  "encoding": "base64",
  "size": 5362,
  "name": "README.md",
  "path": "README.md",
  "content": "encoded content ...",
  "sha": "3d21ec53a331a6f037a91c368710b99387d012c1",
  "url": "https://api.github.com/repos/octokit/octokit.rb/contents/README.md",
  "git_url": "https://api.github.com/repos/octokit/octokit.rb/git/blobs/3d21ec53a331a6f037a91c368710b99387d012c1",
  "html_url": "https://github.com/octokit/octokit.rb/blob/master/README.md",
  "download_url": "https://raw.githubusercontent.com/octokit/octokit.rb/master/README.md",
  "_links": {
    "git": "https://api.github.com/repos/octokit/octokit.rb/git/blobs/3d21ec53a331a6f037a91c368710b99387d012c1",
    "self": "https://api.github.com/repos/octokit/octokit.rb/contents/README.md",
    "html": "https://github.com/octokit/octokit.rb/blob/master/README.md"
  }
}
*
* */

@Keep
data class GithubFileInfo(
    val type: String,
    val name: String,
    val path: String
)

@Keep
data class GithubFileContent(
    val type: String,
    val name: String,
    val path: String,
    // Base64.
    val content: String
)

