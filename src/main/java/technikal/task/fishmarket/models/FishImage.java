package technikal.task.fishmarket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "fish_images")
public class FishImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fish_id", nullable = false)
    private Fish fish;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Fish getFish() {
        return fish;
    }
    public void setFish(Fish fish) {
        this.fish = fish;
    }

}