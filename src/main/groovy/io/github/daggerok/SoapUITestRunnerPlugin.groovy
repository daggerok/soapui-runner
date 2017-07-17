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
package io.github.daggerok

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.github.daggerok.tasks.SoapUITestRunnerTask
import org.gradle.api.Plugin
import org.gradle.api.Project

import static io.github.daggerok.utils.Utils.tryWithLog

@Slf4j
@CompileStatic
@InheritConstructors
class SoapUITestRunnerPlugin implements Plugin<Project> {

  @Override
  void apply(final Project project) {
    createMainTask(project)
    createExtFolder(project)
  }

  private static void createMainTask(final Project project) {
    project.getTasks().create("testrunner", SoapUITestRunnerTask)
  }

  /**
   * SoapUI testrunner looking for ext folder.
   * All inside could be used as external scripts or libraries.
   * <br/>
   * I will create that folder if it's not exists.
   * Otherwise if it's exists and it's not a directory it might cause some unexpected issues...
   *
   * @param project applied to
   */
  private static void createExtFolder(final Project project) {

    final String extDirName = 'ext'
    final File extDir = project.file(extDirName)

    if (extDir.exists()) {
      if (extDir.isDirectory()) return
      log.info "$extDirName is reserved name for SoapUI libraries folder."
      return
    }

    tryWithLog {
      project.mkdir(extDirName)
    }
  }
}
