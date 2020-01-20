package Model;

import java.io.File;

import Controller.ViewInput;

public class ModelMainClass {
	
	ViewInput view;
	Device dev;
	double speed,rotSpeed;
	Camera cam1;
	Mesh[] mesh;
	MeshMaker meshMaker;
	
	public ModelMainClass(ViewInput v) {
		this.view = v;
		mesh = new Mesh[1];
		mesh[0] = new Mesh();
		speed = 2;
		rotSpeed = 4;
		dev = new Device();
		meshMaker = new MeshMaker();
		
		dev.setMeshList(mesh);
		
		cam1 = new Camera();
		cam1.setAngle(0.7);
		cam1.setvPosition(new Vector3(0,0,5));
		cam1.setvUp(new Vector3(0,1,0));
		cam1.setvTarget(new Vector3(0,0,1));
		
		dev.setCam(cam1);
	}
	
	public void moveCamera(int x,int y,int z) {
		cam1.setPosition( cam1.getvPosition().getX()+ x*speed, cam1.getvPosition().getY()+y*speed, cam1.getvPosition().getZ()+z*speed );
		repaint();
	}
	
  	 
	 public void repaint() {
	 dev.refresh();
	 view.display(dev.getBufferedImage());

	 }
	 
	 

	public void initiate(int d, int e) {
		// TODO Auto-generated method stub
		dev.initiate(d, e);
	}

	public void printCam() {
		// TODO Auto-generated method stub
		cam1.getvPosition().print("Camera:");
	}
	
	public boolean makeMesh(File file) {
		
		Mesh[] newMesh = null;
		
		try {
			newMesh =  meshMaker.getMeshFromFile(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(newMesh != null) {
			this.mesh = newMesh;
			dev.setMeshList(mesh);
			
			System.out.println("Mesh set");
			repaint();
			
			return true;
		}else {
			System.out.println("File error");
			return false;
		}
	}

	public void moveCenter(int x,int y,int z) {
		repaint();
	}
	
	public void rotate(int dir) {
		//cam1.setdYaw(cam1.getdYaw()+0.05*dir);
		Vector3 direct = cam1.getvLookDir();
		direct = Vector3.transform(direct, TransformMatrix.makeRotationY(0.1*dir) );
		cam1.setvLookDir(direct);
		repaint();
		
	}

	public void printCenter() {
		// TODO Auto-generated method stub
		cam1.getvPosition().print("Center");
	}

	
	public void rotateUp(int dir) {
		// TODO Auto-generated method stub
		Vector3 direct = cam1.getvLookDir();
		direct = Vector3.transform(direct, TransformMatrix.makeRotationX(0.1*dir) );
		cam1.setvLookDir(direct);
		repaint();
	}
	
	public void forward(int i) {
		// TODO Auto-generated method stub
		if(i>0) cam1.setvPosition(Vector3.vector_add(cam1.getvPosition(), Vector3.multiply(cam1.getvLookDir(), 0.5)));
		else cam1.setvPosition(Vector3.vector_sub(cam1.getvPosition(), Vector3.multiply(cam1.getvLookDir(), 0.5)));
		repaint();
	}
}
