package br.com.irole.api.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "historico_sala_usuario")
public class HistoricoSalaUsuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Sala sala;
	
	@ManyToOne
	@Valid
	private Perfil perfil;
	
	@Column(name = "data_hora_entrada")
	@CreationTimestamp
	private OffsetDateTime data_entrada; 
	
	@Column(name = "data_hora_saida")
	private OffsetDateTime data_saida;
	
	private BigDecimal totalParcial = new BigDecimal(0); 
	
	@JsonProperty(access = Access.READ_ONLY)
	@Transient
	private BigDecimal totalSala; 	
	
	@JsonProperty(access = Access.READ_ONLY)
	@Transient
	private Boolean ativo = true; 	
	
	@JsonProperty(access = Access.READ_ONLY)
	@Transient
	private Long totalUsuarios; 

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

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
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public OffsetDateTime getData_entrada() {
		return data_entrada;
	}

	public void setData_entrada(OffsetDateTime data_entrada) {
		this.data_entrada = data_entrada;
	}

	public OffsetDateTime getData_saida() {
		return data_saida;
	}

	public void setData_saida(OffsetDateTime data_saida) {
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
