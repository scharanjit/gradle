/*
 * Copyright 2017 the original author or authors.
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

package org.gradle.initialization;

import org.gradle.StartParameter;
import org.gradle.api.Transformer;
import org.gradle.api.internal.file.BasicFileResolver;
import org.gradle.internal.Factory;
import org.gradle.internal.buildoption.BooleanBuildOption;
import org.gradle.internal.buildoption.BuildOption;
import org.gradle.internal.buildoption.CommandLineOptionConfiguration;
import org.gradle.internal.buildoption.ListBuildOption;
import org.gradle.internal.buildoption.EnabledOnlyBooleanBuildOption;
import org.gradle.internal.buildoption.StringBuildOption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StartParameterBuildOptionFactory implements Factory<List<BuildOption<StartParameter>>> {

    private final List<BuildOption<StartParameter>> options = new ArrayList<BuildOption<StartParameter>>();

    public StartParameterBuildOptionFactory() {
        options.add(new ProjectCacheDirOption());
        options.add(new RerunTasksOption());
        options.add(new RecompileScriptsOption());
        options.add(new ProfileOption());
        options.add(new ContinueOption());
        options.add(new OfflineOption());
        options.add(new RefreshDependenciesOption());
        options.add(new DryRunOption());
        options.add(new ContinuousOption());
        options.add(new NoProjectDependenciesRebuildOption());
        options.add(new BuildFileOption());
        options.add(new SettingsFileOption());
        options.add(new InitScriptOption());
        options.add(new ExcludeTaskOption());
        options.add(new IncludeBuildOption());
        options.add(new ConfigureOnDemandOption());
        options.add(new BuildCacheOption());
        options.add(new BuildScanOption());
    }

    @Override
    public List<BuildOption<StartParameter>> create() {
        return options;
    }

    public static class ProjectCacheDirOption extends StringBuildOption<StartParameter> {
        public ProjectCacheDirOption() {
            super(null, CommandLineOptionConfiguration.create("project-cache-dir", "Specify the project-specific cache directory. Defaults to .gradle in the root project directory."));
        }

        @Override
        public void applyTo(String value, StartParameter settings, Origin origin) {
            Transformer<File, String> resolver = new BasicFileResolver(settings.getCurrentDir());
            settings.setProjectCacheDir(resolver.transform(value));
        }
    }

    public static class RerunTasksOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public RerunTasksOption() {
            super(null, CommandLineOptionConfiguration.create("rerun-tasks", "Ignore previously cached task results."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setRerunTasks(true);
        }
    }

    public static class RecompileScriptsOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public static final String DEPRECATION_MESSAGE = "Support for --recompile-scripts was deprecated and is scheduled to be removed in Gradle 5.0.";
        public RecompileScriptsOption() {
            super(null, CommandLineOptionConfiguration.create("recompile-scripts", "Force build script recompiling.").deprecated(DEPRECATION_MESSAGE));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setRecompileScripts(true);
        }
    }

    public static class ProfileOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public ProfileOption() {
            super(null, CommandLineOptionConfiguration.create("profile", "Profile build execution time and generates a report in the <build_dir>/reports/profile directory."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setProfile(true);
        }
    }

    public static class ContinueOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public ContinueOption() {
            super(null, CommandLineOptionConfiguration.create("continue", "Continue task execution after a task failure."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setContinueOnFailure(true);
        }
    }

    public static class OfflineOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public OfflineOption() {
            super(null, CommandLineOptionConfiguration.create("offline", "Execute the build without accessing network resources."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setOffline(true);
        }
    }

    public static class RefreshDependenciesOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public RefreshDependenciesOption() {
            super(null, CommandLineOptionConfiguration.create("refresh-dependencies", "Refresh the state of dependencies."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setRefreshDependencies(true);
        }
    }

    public static class DryRunOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public DryRunOption() {
            super(null, CommandLineOptionConfiguration.create("dry-run", "m", "Run the builds with all task actions disabled."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setDryRun(true);
        }
    }

    public static class ContinuousOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public ContinuousOption() {
            super(null, CommandLineOptionConfiguration.create("continuous", "t", "Enables continuous build. Gradle does not exit and will re-execute tasks when task file inputs change.").incubating());
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setContinuous(true);
        }
    }

    public static class NoProjectDependenciesRebuildOption extends EnabledOnlyBooleanBuildOption<StartParameter> {
        public NoProjectDependenciesRebuildOption() {
            super(null, CommandLineOptionConfiguration.create("no-rebuild", "a", "Do not rebuild project dependencies."));
        }

        @Override
        public void applyTo(StartParameter settings, Origin origin) {
            settings.setBuildProjectDependencies(false);
        }
    }

    public static class BuildFileOption extends StringBuildOption<StartParameter> {
        public BuildFileOption() {
            super(null, CommandLineOptionConfiguration.create("build-file", "b", "Specify the build file."));
        }

        @Override
        public void applyTo(String value, StartParameter settings, Origin origin) {
            Transformer<File, String> resolver = new BasicFileResolver(settings.getCurrentDir());
            settings.setBuildFile(resolver.transform(value));
        }
    }

    public static class SettingsFileOption extends StringBuildOption<StartParameter> {
        public SettingsFileOption() {
            super(null, CommandLineOptionConfiguration.create("settings-file", "c", "Specify the settings file."));
        }

        @Override
        public void applyTo(String value, StartParameter settings, Origin origin) {
            Transformer<File, String> resolver = new BasicFileResolver(settings.getCurrentDir());
            settings.setSettingsFile(resolver.transform(value));
        }
    }

    public static class InitScriptOption extends ListBuildOption<StartParameter> {
        public InitScriptOption() {
            super(null, CommandLineOptionConfiguration.create("init-script", "I", "Specify an initialization script."));
        }

        @Override
        public void applyTo(List<String> values, StartParameter settings, Origin origin) {
            Transformer<File, String> resolver = new BasicFileResolver(settings.getCurrentDir());

            for (String script : values) {
                settings.addInitScript(resolver.transform(script));
            }
        }
    }

    public static class ExcludeTaskOption extends ListBuildOption<StartParameter> {
        public ExcludeTaskOption() {
            super(null, CommandLineOptionConfiguration.create("exclude-task", "x", "Specify a task to be excluded from execution."));
        }

        @Override
        public void applyTo(List<String> values, StartParameter settings, Origin origin) {
            settings.setExcludedTaskNames(values);
        }
    }

    public static class IncludeBuildOption extends ListBuildOption<StartParameter> {
        public IncludeBuildOption() {
            super(null, CommandLineOptionConfiguration.create("include-build", "Include the specified build in the composite.").incubating());
        }

        @Override
        public void applyTo(List<String> values, StartParameter settings, Origin origin) {
            Transformer<File, String> resolver = new BasicFileResolver(settings.getCurrentDir());

            for (String includedBuild : values) {
                settings.includeBuild(resolver.transform(includedBuild));
            }
        }
    }

    public static class ConfigureOnDemandOption extends BooleanBuildOption<StartParameter> {
        public static final String GRADLE_PROPERTY = "org.gradle.configureondemand";

        public ConfigureOnDemandOption() {
            super(GRADLE_PROPERTY, CommandLineOptionConfiguration.create("configure-on-demand", "Configure necessary projects only. Gradle will attempt to reduce configuration time for large multi-project builds.").incubating());
        }

        @Override
        public void applyTo(boolean value, StartParameter settings, Origin origin) {
            settings.setConfigureOnDemand(value);
        }
    }

    public static class BuildCacheOption extends BooleanBuildOption<StartParameter> {
        public static final String GRADLE_PROPERTY = "org.gradle.caching";

        public BuildCacheOption() {
            super(GRADLE_PROPERTY, CommandLineOptionConfiguration.create("build-cache", "Enables the Gradle build cache. Gradle will try to reuse outputs from previous builds.").incubating());
        }

        @Override
        public void applyTo(boolean value, StartParameter settings, Origin origin) {
            settings.setBuildCacheEnabled(value);
        }
    }

    public static class BuildScanOption extends BooleanBuildOption<StartParameter> {
        public BuildScanOption() {
            super(null, CommandLineOptionConfiguration.create("scan", "Creates a build scan. Gradle will emit a warning if the build scan plugin has not been applied. (https://gradle.com/build-scans)").incubating());
        }

        @Override
        public void applyTo(boolean value, StartParameter settings, Origin origin) {
            if (value) {
                settings.setBuildScan(true);
            } else {
                settings.setNoBuildScan(true);
            }
        }
    }
}
