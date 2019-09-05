package br.com.irole.api.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKey;
import javax.persistence.Table;

@Entity
@Table(name = "sala")
public class Sala {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ElementCollection
	@JoinTable(name = "pedido_usuario_sala", joinColumns = {
			@JoinColumn(name = "sala_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "pedido_id", referencedColumnName = "id") })
	@MapKey(name = "id")
	private Map<Long, PedidoCliente> pedido = new HashMap<Long, PedidoCliente>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Long, PedidoCliente> getPedido() {
		return pedido;
	}

	public void setPedido(Map<Long, PedidoCliente> pedido) {
		this.pedido = pedido;
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
		Sala other = (Sala) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
