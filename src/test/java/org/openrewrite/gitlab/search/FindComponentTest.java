/*
 * Copyright 2024 the original author or authors.
 * <p>
 * Licensed under the Moderne Source Available License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://docs.moderne.io/licensing/moderne-source-available-license
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.gitlab.search;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.yaml.Assertions.yaml;

class FindComponentTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new FindComponent("\\$CI_SERVER_FQDN/components/opentofu/full-pipeline"));
    }

    @DocumentExample
    @Test
    void exists() {
        //language=yaml
        rewriteRun(
          yaml(
            """
              include:
                - component: $CI_SERVER_FQDN/components/opentofu/full-pipeline@0.10.0
                  inputs:
                    version: 0.10.0
                    opentofu_version: 1.6.1
              """,
            """
              include:
                - ~~>component: $CI_SERVER_FQDN/components/opentofu/full-pipeline@0.10.0
                  inputs:
                    version: 0.10.0
                    opentofu_version: 1.6.1
              """,
            source -> source.path(".gitlab-ci.yml")
          )
        );
    }

    @Test
    void notExists() {
        //language=yaml
        rewriteRun(
          yaml(
            """
              include:
                - component: $CI_SERVER_FQDN/components/opentofu/job-templates@0.10.0
                  inputs:
                    version: 0.10.0
                    opentofu_version: 1.6.1
              """,
            source -> source.path(".gitlab-ci.yml")
          )
        );
    }
}
