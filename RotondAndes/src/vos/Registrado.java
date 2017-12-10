package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Registrado extends Asistente {

	@JsonProperty(value = "nombre")
	private String nombre;
	@JsonProperty(value = "identificacion")
	private String identificacion;
	@JsonProperty(value = "email")
	private String email;
	@JsonProperty(value = "rol")
	private String rol;
	@JsonProperty(value = "preferencia")
	private String preferencia;

	public Registrado(@JsonProperty(value = "id") int id, @JsonProperty(value = "nombre") String nombre,
			@JsonProperty(value = "identificacion") String identificacion, @JsonProperty(value = "email") String email,
			@JsonProperty(value = "rol") String rol, @JsonProperty(value = "preferencia") String preferencia) {
		super(id);
		this.nombre = nombre;
		this.identificacion = identificacion;
		this.email = email;
		this.rol = rol;
		this.preferencia = preferencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getPreferencia() {
		// TODO Auto-generated method stub
		return preferencia;
	}

	public void setPreferencia(String preferencia) {
		this.preferencia = preferencia;
	}

}
