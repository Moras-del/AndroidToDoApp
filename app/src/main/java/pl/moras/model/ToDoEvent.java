package pl.moras.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity(tableName = "todoevent")
public class ToDoEvent {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "opis")
    private String opis;

    @ColumnInfo(name = "datastart")

    private LocalDateTime datastart;

    @ColumnInfo(name = "datakoniec")
    private LocalDateTime datakoniec;

    public ToDoEvent() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpis() {
        return this.opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public LocalDateTime getDatastart() {
        return this.datastart;
    }

    public void setDatastart(LocalDateTime datastart) {
        this.datastart = datastart;
    }

    public LocalDateTime getDatakoniec() {
        return this.datakoniec;
    }

    public void setDatakoniec(LocalDateTime datakoniec) {
        this.datakoniec = datakoniec;
    }


}
