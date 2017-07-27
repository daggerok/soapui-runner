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

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

class ConfigurationUtils {

  static final String CONFIGURATION_NAME = 'extDir'
  /**
   * @param project plugin applied project
   * @return extDir configuration
   */
  static Configuration createExtDirConfiguration(final Project project) {
    def config = project.configurations.maybeCreate(CONFIGURATION_NAME)
    config.transitive = false
    return config
  }

  /**
   * @param project plugin applied project
   * @return true if extDir configuration exists
   */
  static boolean hasNoConfig(final Project project) {
    null == getByProject(project)
  }

  /**
   * load external libraries for SoapUI project execution
   *
   * @param project plugin applied project
   */
  static void loadExtLibraries(final Project project) {
    getByProject(project).collect({ File file ->
        GroovyObject.class.classLoader.addURL(file.toURI().toURL())
    })
  }

  /**
   * @param project plugin applied project
   * @return configuration.
   */
  static Configuration getByProject(final Project project) {
    project.configurations[CONFIGURATION_NAME]
  }

  private ConfigurationUtils() {}
}
