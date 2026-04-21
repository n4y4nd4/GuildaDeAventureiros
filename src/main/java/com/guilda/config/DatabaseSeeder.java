package com.guilda.config;

import com.guilda.audit.entity.Organizacao;
import com.guilda.audit.entity.Usuario;
import com.guilda.audit.entity.UsuarioStatus;
import com.guilda.audit.repository.OrganizacaoRepository;
import com.guilda.audit.repository.UsuarioRepository;
import com.guilda.aventura.entity.Aventureiro;
import com.guilda.aventura.entity.Companheiro;
import com.guilda.aventura.enums.ClasseAventureiro;
import com.guilda.aventura.enums.EspecieCompanheiro;
import com.guilda.aventura.repository.AventureiroRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    private final AventureiroRepository aventureiroRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public DatabaseSeeder(AventureiroRepository aventureiroRepository,
                          OrganizacaoRepository organizacaoRepository,
                          UsuarioRepository usuarioRepository) {
        this.aventureiroRepository = aventureiroRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostConstruct
    public void seed() {
        if (aventureiroRepository.count() >= 100) return;

        Organizacao org = organizacaoRepository.findAll().stream()
            .findFirst()
            .orElseGet(() -> organizacaoRepository.save(new Organizacao("Guilda Principal")));

        Usuario usuario = usuarioRepository.findAll().stream()
            .findFirst()
            .orElseGet(() -> usuarioRepository.save(
                new Usuario(org, "Admin Seed", "seed@guilda.com", "hash_seed", UsuarioStatus.ATIVO)));

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
            Aventureiro a = new Aventureiro(org, usuario, nomes[i % nomes.length],
                classes[i % classes.length], (i % 20) + 1);
            a.setAtivo(i % 7 != 0);
            aventureiroRepository.save(a);

            if (i % 3 == 0) {
                Companheiro c = new Companheiro(a, "Companheiro-" + a.getId(),
                    especies[i % especies.length], (i * 13) % 101);
                a.setCompanheiro(c);
                aventureiroRepository.save(a);
            }
        }

        System.out.println("[DatabaseSeeder] " + aventureiroRepository.count()
            + " aventureiros inseridos no banco.");
    }
}
