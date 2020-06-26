/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.plugins.rust.api.RustFileScanner;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.ast.visitors.LinesOfCodeVisitor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Measurer extends SubscriptionVisitor{

    private final SensorContext sensorContext;
    private final NoSonarFilter noSonarFilter;
    private InputFile sonarFile;

    public Measurer(SensorContext context, NoSonarFilter noSonarFilter) {
        this.sensorContext = context;
        this.noSonarFilter = noSonarFilter;
    }

    public class TestFileMeasurer implements RustFileScanner {
        @Override
        public void scanFile(RustFileScannerContext context) {
            sonarFile = context.getInputFile();

        }
    }


    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.EXPRESSION);
    }

    @Override
    public void scanFile(RustFileScannerContext context) {
        sonarFile = context.getInputFile();
        super.setContext(context);
        scanTree(context.getTree());

        saveMetricOnFile(CoreMetrics.NCLOC, new LinesOfCodeVisitor().linesOfCode(context.getTree()));
    }

    private <T extends Serializable> void saveMetricOnFile(Metric<T> metric, T value) {
        sensorContext.<T>newMeasure().forMetric(metric).on(sonarFile).withValue(value).save();
    }
}
