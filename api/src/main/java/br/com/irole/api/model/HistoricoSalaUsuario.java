package br.com.irole.api.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "historico_sala_usuario")
public class HistoricoSalaUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Sala sala;
	
	@ManyToOne
	private Usuario usuario;
	
	@Column(name = "data_hora_entrada")
	@CreationTimestamp
	private Timestamp data_entrada; 
	
	@Column(name = "data_hora_saida")
	private Timestamp data_saida;
	
	private BigDecimal totalParcial = new BigDecimal(0); 
	
	@Transient
	private BigDecimal totalSala; 
	
	@Transient
	private Long totalUsuarios; 

	public Long getTotalUsuarios() {
		return totalUsuarios;
	}

	public void setTotalUsuarios(Long totalUsuarios) {
		this.totalUsuarios = totalUsuarios;
	}

	public BigDecimal getTotalSala() {
		return totalSala;
	}

	public void setTotalSala(BigDecimal totalSala) {
		this.totalSala = totalSala;
	}

	public BigDecimal getTotalParcial() {
		return totalParcial;
	}

	public void setTotalParcial(BigDecimal totalParcial) {
		this.totalParcial = totalParcial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Timestamp getData_entrada() {
		return data_entrada;
	}

	public void setData_entrada(Timestamp data_entrada) {
		this.data_entrada = data_entrada;
	}

	public Timestamp getData_saida() {
		return data_saida;
	}

	public void setData_saida(Timestamp data_saida) {
		this.data_saida = data_saida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoSalaUsuario other = (HistoricoSalaUsuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
