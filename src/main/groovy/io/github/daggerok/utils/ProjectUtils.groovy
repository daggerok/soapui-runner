/**
 * The MIT License
 *
 * Copyright 2017 Maksim Kostromin
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * https://tldrlegal.com/license/mit-license
 */
package io.github.daggerok.utils

import groovy.transform.CompileStatic
import org.gradle.api.Project

import java.nio.file.Paths

@CompileStatic
class ProjectUtils {

  static final String DEFAULT_PROJECT_FILE = 'soapui-test-project.xml'
  static final String DEFAULT_OUTPUT_DIR = 'soapui'
  static final String DEFAULT_EXT_DIR = 'ext'

  /**
   * <ol>
   *   <li>skip if no extDir configuration exists</li>
   *   <li>configure classpath for any libraries from soapUIExtLibraries config</li>
   *   <li>create create "extDir" task to do generate needed ext folder for project execution</li>
   * </ol>
   *
   * @param project plugin applied project
   */
  static void afterEvaluate(final Project project) {
    project.afterEvaluate {
      if (ConfigurationUtils.hasNoConfig(project)) return
      ConfigurationUtils.loadExtLibraries(project)
      TaskUtils.createExtDirTask(project)
    }
  }

  /**
   * @param project plugin applied project
   * @return default soapui.ext.libraries path
   */
  static String defaultProjectFile(final Project project) {
    resolvePath(project.projectDir.absolutePath, DEFAULT_PROJECT_FILE)
  }

  /**
   * @param project plugin applied project
   * @return default soapui.ext.libraries path
   */
  static String defaultOutputPath(final Project project) {
    resolvePath(project.rootProject.buildDir.absolutePath, DEFAULT_OUTPUT_DIR)
  }

  /**
   * @param project plugin applied project
   * @return default soapui.ext.libraries path
   */
  static String extDirPath(final Project project) {
    resolvePath(defaultOutputPath(project), DEFAULT_EXT_DIR)
  }

  private static String resolvePath(final String parent, final String child) {
    Paths.get(parent, child).toString()
  }

  private PluginUtils() {}
}
