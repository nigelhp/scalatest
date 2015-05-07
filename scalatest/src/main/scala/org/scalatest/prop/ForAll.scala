/*
 * Copyright 2001-2015 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.prop

import scala.annotation.tailrec

object ForAll extends Configuration {
  def forAll[A](fun: (A) => Unit)
    (implicit 
      config: PropertyCheckConfig,
      genA: org.scalatest.prop.Gen[A]
    ): Unit = {
      val (v, _) = genA.next()
      fun(v)
     @tailrec
     def loop(succeededCount: Int, nextRnd: Rnd): Unit = {
       val (v, r) = genA.next(rnd = nextRnd)
       fun(v)
       val nextCount = succeededCount + 1
       if (nextCount < config.minSuccessful)
         loop(nextCount, r)
    }
    loop(1, Rnd.default)
  }
}
