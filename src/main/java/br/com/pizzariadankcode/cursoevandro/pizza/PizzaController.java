package br.com.pizzariadankcode.cursoevandro.pizza;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/pizzas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class PizzaController {

    private final PizzaService pizzaService;

    @PostMapping
    public ResponseEntity<PizzaDTO> cadastrar(@RequestBody @Valid PizzaDTO dto, UriComponentsBuilder uriBuilder) {
        PizzaDTO pizzaDTO = pizzaService.criarPizza(dto);
        URI endereco = uriBuilder.path("/pizzas/{id}")
                .buildAndExpand(pizzaDTO.getId())
                .toUri();
        return ResponseEntity.created(endereco).body(pizzaDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PizzaDTO>> buscarTodos(@PageableDefault(size = 10) Pageable paginacao) {
        return ResponseEntity.ok(pizzaService.buscarTodos(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDTO> buscaPorId(@PathVariable @NotNull Long id) {

        PizzaDTO pizzaDTO = pizzaService.buscaPorId(id);

        return ResponseEntity.ok(pizzaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDTO> atualiza(@PathVariable @NotNull Long id,
                                             @RequestBody @Valid PizzaDTO dto) {
        PizzaDTO pizzaAtualizada = pizzaService.atualizarPizza(id, dto);

        return ResponseEntity.ok(pizzaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable @NotNull Long id) {
        pizzaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
