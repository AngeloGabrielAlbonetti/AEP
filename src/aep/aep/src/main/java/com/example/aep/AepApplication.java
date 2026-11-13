package com.example.aep;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

@SpringBootApplication
public class AepApplication {

	public static void main(String[] args) {
		SpringApplication.run(AepApplication.class, args);
	}

	private static <TIPO> TIPO busca(EntityManager entityManager, Scanner scanner, Class<TIPO> tipoClass, String bq){
		List<TIPO> tipos = entityManager.createQuery(bq,tipoClass).getResultList();

		if(tipos.isEmpty()){
			System.out.println("Nada Encontrado");
			return null;
		}else {
			System.out.println("ID "+tipoClass.getSimpleName()+ " da busca:");
			for (TIPO tipo: tipos)
				System.out.println("ID: "+entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(tipo));

			TIPO tipo = entityManager.find(tipoClass,Integer.parseInt(scanner.nextLine().trim()));
			System.out.println(tipo);
			return tipo;
		}
	}




	@Bean
	CommandLineRunner inicio(EntityManagerFactory entityManagerFactory){
		return args -> {
			Scanner scanner = new Scanner(System.in);
			DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			Function<Lote, String>loteStringFunction = lote -> lote.getId()+"|"+lote.getNomeProduto() + " | " + lote.getQuantidade() + " " + lote.getUnidadeDeMedida() + " | R$ " + lote.getPreco() + " | vence " + lote.getDataValidade().format(date) + " (" + lote.tempoPraVencimento() + " dias) | " + lote.validar() + " | " + lote.getEstabelecimento().getNome();
			int opc = 0;
			while (opc !=11){
				System.out.println("1  - Cadastrar usuário");
				System.out.println("2  - Cadastrar estabelecimento");
				System.out.println("3  - Cadastrar lote");
				System.out.println("4  - Listar lotes");
				System.out.println("5  - Atualizar lote");
				System.out.println("6  - Excluir lote");
				System.out.println("7  - Filtrar lotes por validade");
				System.out.println("8  - Registrar doação");
				System.out.println("9  - Registrar descarte");
				System.out.println("10 - Listar doações e descartes");
				System.out.println("11  - Sair");
				System.out.printf("opção:");

				EntityManager entityManager = entityManagerFactory.createEntityManager();
				try {
					opc = Integer.parseInt(scanner.nextLine().trim());
				} catch (RuntimeException e) {
					opc=0;
				}
				try {
					entityManager.getTransaction().begin();


					switch (opc) {
						case 1:
							String nome, email, codico;
							int nivel;
							System.out.printf("Nome:");
							nome = scanner.nextLine();
							System.out.printf("Email:");
							email = scanner.nextLine();
							System.out.printf("Nivel Adiminitrador(1) | Operador(2)");
							nivel = Integer.parseInt(scanner.nextLine().trim());
							if (nivel == 1) {
								Administrador administrador = new Administrador(nome, email);
								entityManager.persist(administrador);
								entityManager.flush();
								System.out.println("Id adm"+administrador.getId());
							}
							else if (nivel == 2) {
								Operador operador = new Operador(nome, email);
								entityManager.persist(operador);
								entityManager.flush();
								System.out.println("Id Operadpr"+operador.getId());
							}
							else
								throw new IllegalArgumentException("Digito invalido");

							break;

						case 2:
							List<Administrador> administradors = entityManager.createQuery("select a from Administrador a order by a.id", Administrador.class).getResultList();
							if (administradors.isEmpty())
								System.out.printf("\n Cadastre um administrador");
							else {

								administradors.forEach(administrador -> System.out.println("" + administrador.getId() + " " + administrador.getNome()));
								System.out.printf("ID ADM: ");
								Administrador ad = entityManager.find(Administrador.class, Integer.parseInt(scanner.nextLine().trim()));

								System.out.printf("Nome do Estabelecimento");
								nome = scanner.nextLine();
								System.out.println("Codico: ");
								codico = scanner.nextLine();
								Estabelecimento estabelecimento = new Estabelecimento(ad, nome, codico);
								entityManager.persist(estabelecimento);
								System.out.println("Estabelecimento cadastrado");
							}
							break;
						case 3:
							String nomedproduto, ctg, unt;
							int qt;
							LocalDate val;
							BigDecimal preco;
							Estabelecimento estabelecimento = busca(entityManager, scanner, Estabelecimento.class, "select estabelecimento from Estabelecimento estabelecimento order by estabelecimento.id");
							Operador operador = busca(entityManager, scanner, Operador.class, "select operador from Operador operador order by operador.id");

							System.out.printf("\nDigite o nome do produto:");
							nomedproduto = scanner.nextLine();

							System.out.printf("\nDigite a categoria:");
							ctg = scanner.nextLine();

							System.out.printf("\nDigite quantidade:");
							qt = Integer.parseInt(scanner.nextLine().trim());

							System.out.printf("\nDigite a unidade de medida:");
							unt = scanner.nextLine();

							System.out.printf("\nDigite o preço");
							preco = new BigDecimal(scanner.nextLine().trim().replace(",", "."));

							System.out.printf("\nDigite a validade dd/MM/aaaa:");
							val = LocalDate.parse(scanner.nextLine().trim(), date);


							estabelecimento.adicionarLotes(estabelecimento, operador, nomedproduto, ctg, qt, unt, preco, val);
							entityManager.flush();
							Lote lote = estabelecimento.getLotes().get(estabelecimento.getLotes().size() - 1);
							System.out.printf("\n Lote cadastrado %d", lote.getId());
							break;
						case 4:
							List<Lote> lotes = entityManager.createQuery("select lote from Lote lote order by lote.dataValidade", Lote.class).getResultList();
							if (lotes.isEmpty())
								System.out.println("Não possui um lote");
							else
								for (Lote lote1 : lotes)
									System.out.println(lote1.getId()+"|"+lote1.getEstabelecimento().getNome() + "|" + lote1.getRegistrarBaixa().getNome() + "|" + lote1.getNomeProduto() + "|" + lote1.getCategoria() + "|" + lote1.getQuantidade() + "|" + lote1.getUnidadeDeMedida() + "|" + lote1.getPreco() + "|" + lote1.getDataValidade());
							break;


						case 5:
							Lote lote1 = busca(entityManager, scanner, Lote.class, "select lote from Lote lote order by lote.id");
							System.out.printf("\nDigite o nome do produto:");
							lote1.setNomeProduto(scanner.nextLine());

							System.out.printf("\nDigite a categoria:");
							lote1.setCategoria(scanner.nextLine());

							System.out.printf("\nDigite quantidade:");
							lote1.setQuantidade(Integer.parseInt(scanner.nextLine().trim()));

							System.out.printf("\nDigite a unidade de medida:");
							lote1.setUnidadeDeMedida(scanner.nextLine());

							System.out.printf("\nDigite o preço");
							lote1.setPreco(new BigDecimal(scanner.nextLine().trim().replace(",", ".")));

							System.out.printf("\nDigite a validade dd/MM/aaaa:");
							lote1.setDataValidade(LocalDate.parse(scanner.nextLine().trim(), date));
							break;

						case 6:
							Lote lote2 = busca(entityManager, scanner, Lote.class, "select lote from Lote lote order by lote.id");
							long des_doa = entityManager.createQuery("select count(d) from Doacao d where d.possui = :l", Long.class).setParameter("l", lote2).getSingleResult() + entityManager.createQuery("select count(d) from Descarte d where d.possui = :l", Long.class).setParameter("l", lote2).getSingleResult();
							if (des_doa>0){
								System.out.println("Esse lote não pode ser excluido porque possui doações ou descartes");
							}else {
								if (lote2 != null) {
									System.out.println("Excluir (S) ou (N)|" + loteStringFunction.apply(lote2));
									if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
										entityManager.remove(lote2);
										System.out.println("Lote excluido");
									} else
										System.out.println("Exclusão não realizada");

								}
							}
							break;
						case 7:
							System.out.printf("Situação ");
							List<Lote> loteList = entityManager.createQuery("select l from Lote l order by l.dataValidade", Lote.class).getResultList();
							if (!loteList.isEmpty()){
								for (Lote l : loteList) {
									System.out.println(l.getNomeProduto()+"|"+l.validar()+"|"+l.getDataValidade()+"|"+l.tempoPraVencimento());
								}
							}else
								System.out.println("Não existe lote");
							break;
						case 8:

							break;
						case 9:

							break;

						case 10:
							List<Doacao> doacoes = entityManager.createQuery("select doacao from Doacao doacao order by doacao.date desc", Doacao.class).getResultList();
							List<Descarte> descartes = entityManager.createQuery("select des from Descarte des order by des.date desc", Descarte.class).getResultList();
							double vDescarte = 0, vDoacao = 0;
							int qDescarte = 0, qDoacao = 0;
							if (!doacoes.isEmpty()) {
								for (Doacao i : doacoes) {
									System.out.println("Entidades|" + i.getEntidades() + "|Tipo|" + i.getTipo() + "|Data|" + i.getDate() + "|Possui|" + i.getPossui() + "|quantidade|" + i.getQuantidade());
									System.out.println("Impaquito R$ " + i.calcularImpacto());
									vDoacao += i.calcularImpacto();
									qDoacao+=i.getQuantidade();
								}

							} else
								System.out.println("Não possui doção realizada ");

							if (!descartes.isEmpty()) {
								for (Descarte i : descartes) {
									System.out.println("Justificativa|" + i.getJustificativa() + "|Tipo|" + i.getTipo() + "|Data|" + i.getDate() + "|Possui|" + i.getPossui() + "|quantidade|" + i.getQuantidade());
									System.out.println("Impaquito R$ " + i.calcularImpacto());
									vDescarte += i.calcularImpacto();
									qDescarte+= i.getQuantidade();
								}
							}

							if ((qDescarte + qDoacao) != 0) {
								System.out.println("Soma de valor de itens doados foi R$ " + vDoacao);
								System.out.println("Soma de valor de itens descartados foi R$ " + vDescarte);
								System.out.println("Tota de itens doados e descartados" + qDescarte + qDoacao);
							}
							break;

					}
					entityManager.getTransaction().commit();
				} catch (Exception e) {
					if (entityManager.getTransaction().isActive())
						entityManager.getTransaction().rollback();

					System.out.println("Erro "+e.getMessage());
				}finally {
					entityManager.close();
				}

			};
			System.exit(0);

		};
	}
}



