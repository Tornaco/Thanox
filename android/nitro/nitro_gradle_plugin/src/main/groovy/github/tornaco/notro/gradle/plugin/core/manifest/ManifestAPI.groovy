/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package github.tornaco.notro.gradle.plugin.core.manifest

import com.android.build.gradle.tasks.ProcessApplicationManifest
import github.tornaco.notro.gradle.plugin.core.AGPCompat
import github.tornaco.notro.gradle.plugin.core.Logger
import org.gradle.api.GradleException
import org.gradle.api.Project

import java.util.regex.Pattern

/**
 * @author RePlugin Team
 */
class ManifestAPI {

    IManifest sManifestAPIImpl

    def getActivities(Project project, String variantDir) {
        if (sManifestAPIImpl == null) {
            sManifestAPIImpl = new ManifestReader(manifestPath(project, variantDir))
        }
        sManifestAPIImpl.activities
    }

    /**
     * 获取 AndroidManifest.xml 路径
     */
    def static manifestPath(Project project, String variantDir) {
        // Compatible with path separators for window and Linux, and fit split param based on 'Pattern.quote'
        def variantDirArray = variantDir.split(Pattern.quote(File.separator))
        String variantName = ""
        variantDirArray.each {
            //首字母大写进行拼接
            variantName += it.capitalize()
        }
        Logger.log "variantName:${variantName}"

        def manifestTask = AGPCompat.getProcessManifestTask(project, variantName)
        Logger.log "manifestTask:${manifestTask}"

        //如果processManifestTask存在的话
        //transform的task目前能保证在processManifestTask之后执行
        //正常的manifest
        File manifestOutputFile = AGPCompat.getMergedManifestFileCompat(manifestTask)
        Logger.log "manifestOutputFile:${manifestOutputFile}"

        if (manifestOutputFile == null) {
            throw new GradleException("can't get manifest file")
        }

        //打印
        Logger.log "manifestOutputFile:${manifestOutputFile} ${manifestOutputFile.exists()}"

        //先设置为正常的manifest
        File result = manifestOutputFile

        //最后检测文件是否存在，打印
        if (!result.exists()) {
            Logger.log 'AndroidManifest.xml not exist'
        }
        //输出路径
        Logger.log "AndroidManifest.xml path：$result"

        return result.absolutePath
    }
}
