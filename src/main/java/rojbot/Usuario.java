package rojbot;

import java.util.Date;

public class Usuario {
	  private int id;
	  private String nombre;
	  private String apellido1;	
	  private String apellido2;
	  private String correo;
	  private String usutelegram;
	  private Date fechaAlta;
	  private Date fechaModif;
	  private Date fechaBaja;
	  
	  public Usuario(int id, String nombre, String apellido1, String apellido2, String correo, String usutelegram,
			Date fechaAlta, Date fechaModif, Date fechaBaja) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.correo = correo;
		this.usutelegram = usutelegram;
		this.fechaAlta = fechaAlta;
		this.fechaModif = fechaModif;
		this.fechaBaja = fechaBaja;
	}
		   
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getApellido1() {
	        return apellido1;
	    }

	    public void setApellido1(String apellido1) {
	        this.apellido1 = apellido1;
	    }
	    
	    public String getApellido2() {
	        return apellido2;
	    }

	    public void setApellido2(String apellido2) {
	        this.apellido2 = apellido2;
	    }
	    
	    public String getUsutelegram() {
			return usutelegram;
		}

		public void setUsutelegram(String usutelegram) {
			this.usutelegram = usutelegram;
		}
		
		public String getCorreo() {
			return correo;
		}

		public void setCorreo(String correo) {
			this.correo = correo;
		}
		
		public Date getFechaAlta() {
			return fechaAlta;
		}

		public void setFechaAlta(Date fechaAlta) {
			this.fechaAlta = fechaAlta;
		}

		public Date getFechaModif() {
			return fechaModif;
		}

		public void setFechaModif(Date fechaModif) {
			this.fechaModif = fechaModif;
		}

		public Date getFechaBaja() {
			return fechaBaja;
		}

		public void setFechaBaja(Date fechaBaja) {
			this.fechaBaja = fechaBaja;
		}

}
