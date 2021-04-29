package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FinalizarLeilaoServiceTest {

    private FinalizarLeilaoService service;

    @Mock // apenas indica que um atributo será um mock.
    private LeilaoDao leilaoDao;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(leilaoDao);
    }

    @Test
    public void deveriaFinalizarUmLeilao() {
        List<Leilao> leiloes = leiloes();
        Mockito.when(leilaoDao.buscarLeiloesExpirados())
                .thenReturn(leiloes);
        service.finalizarLeiloesExpirados();
        Leilao leilao = leiloes.get(0);

        assertTrue(leilao.isFechado());
        assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
        Mockito.verify(leilaoDao).salvar(leilao); // verificar se o método salvar foi chamado passando o parâmetro
    }

    private List<Leilao> leiloes() {
        List<Leilao> listLeiloes = new ArrayList<>();

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Joao"),
                new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Flavio"),
                new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        listLeiloes.add(leilao);

        return listLeiloes;

    }
}
