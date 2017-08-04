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

import groovy.util.logging.Slf4j
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy

import static io.github.daggerok.utils.ConfigurationUtils.CONFIGURATION_NAME

@Slf4j
class TaskUtils {

  static String GROUP = 'SoapUI runner'

  /**
   * @param project plugin applied project
   * @return extDir task.
   */
  static Task createExtDirTask(final Project project) {

    // read more: https://github.com/daggerok/soapui-runner/issues/1
    if (project.getTasksByName(CONFIGURATION_NAME, true).size() > 0) return

    // create extDit task only if it's not exists
    log.info "create $CONFIGURATION_NAME task for $project.name project"

    project.getTasks().create(name: CONFIGURATION_NAME, type: Copy, group: GROUP, description: describe(CONFIGURATION_NAME)) {
      from ConfigurationUtils.getByProject(project)
      into ProjectUtils.extDirPath(project)
    }
  }

  /**
   * @param id task identifier
   * @return task descripotion
   * @see `gradle tasks --all`
   */
  static String describe(final String id) {
    "SoapUI $id task"
  }

  private TaskUtils() {}
}
