package pe.edu.vallegrande.imagen.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Map; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.vallegrande.imagen.service.ImageGenerationService;

@RestController
public class ImageController {

    private final ImageGenerationService imageGenerationService;

    public ImageController(ImageGenerationService imageGenerationService) {
        this.imageGenerationService = imageGenerationService;
    }

    @PostMapping("/chat-bot")
        public ResponseEntity<?> generateImage3D(@Valid @RequestBody ImageRequest imageRequest) {
            try {
                String response = imageGenerationService.generateImage3D(imageRequest.getInput());
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : "Error desconocido";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error generating 3D image", "detail", errorMsg));
            }
        }

}

class ImageRequest {
    @NotEmpty(message = "Input cannot be empty")
    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
