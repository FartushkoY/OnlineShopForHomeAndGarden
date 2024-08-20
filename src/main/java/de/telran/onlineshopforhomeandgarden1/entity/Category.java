package de.telran.onlineshopforhomeandgarden1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "category_id")
    private Long id;

    private String name;

    @Column(name = "image_url")
    private String imageUrl;

//    @OneToMany(mappedBy = "category")
//    @JsonIgnore
////    @JsonManagedReference("category")
//    private List<Product> products;

}
