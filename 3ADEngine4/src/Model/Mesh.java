package Model;

public class Mesh {
	
	
	private String Name;
    private Vector3[] Vertices;
    private Face[] Faces;
    private Vector3 Position;
    private Vector3 Rotation;
    
    ///CUBE
    public Mesh() {
    	 setVertices(new Vector3[8]);
    	 setFaces(new Face[12]);
         setName("cube");
         
         Vertices[0] = new Vector3(-1, 1, 1);
         Vertices[1] = new Vector3(1, 1, 1);
         Vertices[2] = new Vector3(-1, -1, 1);
         Vertices[3] = new Vector3(-1, -1, -1);
         Vertices[4] = new Vector3(-1, 1, -1);
         Vertices[5] = new Vector3(1, 1, -1);
         Vertices[6] = new Vector3(1, -1, 1);
         Vertices[7] = new Vector3(1, -1, -1);

         Faces[0] = new Face (0,1,2);
         Faces[1] = new Face (1,2,6);
         
         Faces[2] = new Face (0,2,4);
         Faces[3] = new Face (2,4,3);
         
         Faces[4] = new Face (4,3,5);
         Faces[5] = new Face (3,5,7);

         Faces[6] = new Face (1,5,6);
         Faces[7] = new Face (5,6,7);
         
         Faces[8] = new Face (0,1,4);
         Faces[9] = new Face (1,4,5);
         
         Faces[10] = new Face (2,3,6);
         Faces[11] = new Face (3,7,6);
 		
 		Position = new Vector3(0,0,0);
 		Rotation = new Vector3(0,0,0);
    }
    
    public Mesh(String name,Vector3[] v, Face[] f) {
    	setVertices(v);
    	setFaces(f);
        setName(name);
        Position = new Vector3(0,0,0);
 		Rotation = new Vector3(0,0,0);
    }

    public Mesh(String name, int verticesCount,int FacesCount)
    {
        setVertices(new Vector3[verticesCount]);
        setFaces(new Face[FacesCount]);
        setName(name);
    }

	private void setFaces(Face[] faces2) {
		this.Faces = faces2;
		
	}
	
	public Face[] getFaces() {
		return Faces;
	}
	

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Vector3[] getVertices() {
		return Vertices;
	}

	public void setVertices(Vector3[] vertices) {
		Vertices = vertices;
	}

	public Vector3 getPosition() {
		return Position;
	}

	public void setPosition(Vector3 position) {
		Position = position;
	}

	public Vector3 getRotation() {
		return Rotation;
	}

	public void setRotation(Vector3 rotation) {
		Rotation = rotation;
	}
	
	public void print() {
		for(int i=0;i<Vertices.length;i++)
			Vertices[i].print(i+"");
	}
	

}
