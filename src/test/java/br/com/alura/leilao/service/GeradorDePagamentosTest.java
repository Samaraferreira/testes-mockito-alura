package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GeradorDePagamentosTest {

    private GeradorDePagamento gerador;

    @Mock // apenas indica que um atributo será um mock.
    private PagamentoDao pagamentoDao;

    @Captor // capturar um objeto criado internamente na classe sendo testada
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void initEach() {
        MockitoAnnotations.initMocks(this);
        this.gerador = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    public void deveriaCriarPagamentoParaVencedorDoLeilao() {
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        gerador.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());  // capturar objeto

        Pagamento pagamento = captor.getValue();
        assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
        assertEquals(vencedor.getValor(), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    private Leilao leilao() {

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Flavio"),
                new BigDecimal("900"));

        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);

        return leilao;
    }
}
