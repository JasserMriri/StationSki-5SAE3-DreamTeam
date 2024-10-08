package tn.esprit.spring;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.ISkierServices;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;
import java.util.List;


@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierControllerTest {
    private final ISkierServices skierServices;

}
