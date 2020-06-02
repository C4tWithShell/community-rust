package org.elegoff.rust.utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RustReportIssue {
    private final String ruleId;
    private final List<RustReportLocation> locations;
    private final List<RustReportLocation> flow;

    public RustReportIssue(String ruleId, @Nullable String file, @Nullable String line, String info) {
        super();
        this.ruleId = ruleId;
        this.locations = new ArrayList<>();
        this.flow = new ArrayList<>();
        addLocation(file, line, info);
    }

    public final void addLocation(@Nullable String file, @Nullable String line, String info) {
        locations.add(new RustReportLocation(file, line, info));
    }

    public final void addFlowElement(@Nullable String file, @Nullable String line, String info) {
        flow.add(0, new RustReportLocation(file, line, info));
    }


    public String getRuleId() {
        return ruleId;
    }

    public List<RustReportLocation> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    public List<RustReportLocation> getFlow() {
        return Collections.unmodifiableList(flow);
    }

    @Override
    public String toString() {
        String locationsToString = locations.stream().map(Object::toString).collect(Collectors.joining(", "));
        String flowToString = flow.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "RustReportIssue [ruleId=" + ruleId + ", locations=" + locationsToString + ", flow=" + flowToString + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(locations, flow, ruleId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RustReportIssue other = (RustReportIssue) obj;
        return Objects.equals(locations, other.locations) && Objects.equals(flow, other.flow)
                && Objects.equals(ruleId, other.ruleId);
    }
}
