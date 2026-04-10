package com.guilda.repository;

import com.guilda.enums.ClasseAventureiro;
import com.guilda.enums.EspecieCompanheiro;
import com.guilda.model.Aventureiro;
import com.guilda.model.Companheiro;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AvenureiroRepository {

    private final List<Aventureiro> aventureiros = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        String[] nomes = {
            "Aldric","Brenna","Caius","Dara","Edwyn","Fiona","Gareth","Hilda","Ivan","Jora",
            "Kael","Lyra","Maren","Nolan","Oryn","Petra","Quinn","Reva","Soren","Talia",
            "Uric","Vela","Wren","Xara","Yoren","Zara","Aiden","Bria","Colt","Dwyn",
            "Elan","Frey","Gwen","Holt","Iris","Jace","Kira","Lorn","Mira","Nyx",
            "Orin","Pax","Rael","Syla","Thorn","Ulva","Vorn","Wyla","Xen","Yara",
            "Zorn","Aran","Bael","Cira","Dorn","Elva","Fael","Gorn","Hael","Iorn",
            "Jael","Korn","Lael","Morn","Nael","Oael","Pael","Rorn","Sael","Tael",
            "Uael","Vael","Wael","Xael","Yael","Zael","Aben","Bren","Cren","Dren",
            "Eren","Fren","Gren","Hren","Iren","Jren","Kren","Lren","Mren","Nren",
            "Oren","Pren","Rren","Sren","Tren","Uren","Vren","Wren2","Xren","Yren",
            "Zren","Aorn","Born","Corn"
        };
        ClasseAventureiro[] classes = ClasseAventureiro.values();
        EspecieCompanheiro[] especies = EspecieCompanheiro.values();

        for (int i = 0; i < 104; i++) {
            long id = idSequence.getAndIncrement();
            Aventureiro a = new Aventureiro(id, nomes[i % nomes.length], classes[i % classes.length], (i % 20) + 1);
            a.setAtivo(i % 7 != 0); // ~1 em 7 inativos

            // ~1 em 3 tem companheiro
            if (i % 3 == 0) {
                a.setCompanheiro(new Companheiro(
                    "Companheiro-" + id,
                    especies[i % especies.length],
                    (i * 13) % 101
                ));
            }
            aventureiros.add(a);
        }
    }

    public Aventureiro salvar(Aventureiro aventureiro) {
        if (aventureiro.getId() == null) {
            aventureiro.setId(idSequence.getAndIncrement());
            aventureiros.add(aventureiro);
        } else {
            for (int i = 0; i < aventureiros.size(); i++) {
                if (aventureiros.get(i).getId().equals(aventureiro.getId())) {
                    aventureiros.set(i, aventureiro);
                    return aventureiro;
                }
            }
            aventureiros.add(aventureiro);
        }
        return aventureiro;
    }

    public List<Aventureiro> buscarTodos() {
        return new ArrayList<>(aventureiros);
    }

    public Optional<Aventureiro> buscarPorId(Long id) {
        return aventureiros.stream().filter(a -> a.getId().equals(id)).findFirst();
    }
}
