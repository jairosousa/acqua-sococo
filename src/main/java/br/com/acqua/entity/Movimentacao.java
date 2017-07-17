package br.com.acqua.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 
 * @author Jairo Sousa 06/01/2017
 */

@Entity
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column(name = "data_hora", nullable = false)
	private Date dataHora;

	@Column(name = "observacao")
	private String observacao;

	@Column(name = "situacao")
	private String situacao;

	@Column(name = "lote")
	private String lote;

	@ManyToOne
	private Produto produto;

	@OneToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Movimentacao movimentacao = (Movimentacao) o;
		if (movimentacao.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), movimentacao.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Movimentacao{" + "id=" + getId() + ", dataHora='" + getDataHora() + "'" + ", observacao='"
				+ getObservacao() + "'" + ", situacao='" + getSituacao() + "'" + ", lote='" + getLote() + "'" + "}";
	}
}
