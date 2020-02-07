package org.wincc.report.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "view_report")
public class Report {

    @Id
    private Long routeRunId;

    @Column(name = "route_id")
    private Integer routeId;

    @Column(name = "date_time_start")
    private LocalDateTime dateStart;

    @Column(name = "date_time_end")
    private LocalDateTime dateEnd;

    @Column(name = "time_work")
    private Integer timeWork;

    @Column(name = "el_num")
    private Integer elNum;

    @Column(name = "name")
    private String name;

    @Column(name = "hrs")
    private Integer hrs;

    @Column(name = "avg_load")
    private Integer avrLoad;

    @Column(name = "prf")
    private Integer prf;

    @Column(name = "id_alarm")
    private String alarm;

    @Column(name = "pal")
    private Integer pal;

    @Column(name = "t_max")
    private Integer tMax;

    @Column(name = "t_belt_max")
    private Integer tBeltMax;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(routeRunId, report.routeRunId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeRunId);
    }
}
