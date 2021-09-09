package uz.zako.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryPayload {
    private UUID id;

    private String name;

    private String username;
}
