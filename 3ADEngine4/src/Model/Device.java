package Model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class Device {

	BufferedImage bmp;
	
	Graphics2D g2d;
	TransformMatrix viewMatrix;
	Camera cam;
	Mesh[] meshList;
	
	public Device() {
		cam = new Camera();
		meshList = new Mesh[1];
		meshList[0] = new Mesh();
	}
	
	public Device(Camera c, Mesh[] m ) {
		cam = c;
		meshList = m;
	}
	
	public void setMeshList(Mesh[] m) {
		this.meshList = m;
	}
	
	public void setMeshList(Mesh m) {
		this.meshList = new Mesh[1];
		meshList[0] = m;
	}
	
	public void setCam(Camera c) {
		this.cam = c;
	}
	
	public void initiate(int w,int h) {
		bmp = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void refresh() {
		render(cam,meshList);
	}
	
	// Clear the back buffer with a specific color
	 public void Clear(Color c) {
         g2d = (Graphics2D) bmp.getGraphics();
         g2d.setColor(c);
         g2d.fillRect(0, 0, bmp.getWidth(), bmp.getHeight());
     }
	     
     // Called to put a pixel on screen at a specific X,Y coordinates
     public void PutPixel(int x, int y, Color color)
     {
         bmp.setRGB(x, y, color.getRGB());
     }
     
     //Drawing tirangle
     public Path2D path(Vector2 v1,Vector2 v2,Vector2 v3) {
    	 Path2D path = new Path2D.Double();
    	 path.moveTo(v1.getX(), v1.getY());
    	 path.lineTo(v2.getX(), v2.getY());
    	 path.lineTo(v3.getX(), v3.getY());
    	 path.closePath();
		return path;
     }
     
     
     //Drawinig line
     public Path2D path(Vector2 v1,Vector2 v2) {
    	 Path2D path = new Path2D.Double();
    	 path.moveTo(v1.getX(), v1.getY());
    	 path.lineTo(v2.getX(), v2.getY());
    	 path.closePath();
		return path;
     }
     
     // Transforming Vector3 coord with TransformMatrix and project on 2D screen
     public Vector2 Project(Vector3 coord, TransformMatrix transMat)
     {
         Vector3 point = Vector3.transform(coord, transMat);
         
        double x = (bmp.getWidth()/2.0f+ (  point.getX()* bmp.getHeight()/(point.getZ())  ));
        double y = (bmp.getHeight()/2.0f- ( point.getY()* bmp.getHeight()/(point.getZ()) ));
         
         return (new Vector2(x, y));
     }
     
     public void DrawPoint(Vector2 point)
     {
    	
         if (point.getX() >= 0 && point.getY() >= 0 && point.getX() < bmp.getWidth() && point.getY() < bmp.getHeight())
         {
             PutPixel(Math.round( Math.round( point.getX() ) ), Math.round( Math.round( point.getY() ) ), Color.white);
         }
     }
     
     public void DrawPoint(Vector2 point,Color c)
     {
         if (point.getX() >= 0 && point.getY() >= 0 && point.getX() < bmp.getWidth() && point.getY() < bmp.getHeight())
         {
             PutPixel((int)point.getX(), (int)point.getY(), c);
         }
     }
     
     
        
     public void render(Camera cam, Mesh[] meshes) {
    	 
    	Clear(Color.BLACK);
    	
    	TransformMatrix matProj = TransformMatrix.makeProjection(cam.getAngle(),( 9.0 / 16.0), cam.getdNear(), cam.getdFar());
    	
    	for(int i=0;i<meshes.length;i++) {
    		
    		Mesh mesh = meshes[i];
    		
    		TransformMatrix matRotZ,matRotX,matRotY;
    		
    		matRotZ = TransformMatrix.makeRotationZ(cam.getdTheta()*0.5);
    		matRotX = TransformMatrix.makeRotationX(cam.getdTheta());
    		matRotY = TransformMatrix.makeRotationY(cam.getdYaw());
    		
    		TransformMatrix matTrans;
    		matTrans = TransformMatrix.makeTranslation(0,0,5);
    		
    		TransformMatrix matWorld;
    		matWorld = TransformMatrix.getDiagonalMatrix();
    		matWorld = TransformMatrix.Matrix_MultiplyMatrix(matRotZ, matRotX);
    		matWorld = TransformMatrix.Matrix_MultiplyMatrix(matWorld, matRotY);
    		matWorld = TransformMatrix.Matrix_MultiplyMatrix(matWorld, matTrans);
    		
    		TransformMatrix matCameraRot = TransformMatrix.makeRotationY(cam.getdYaw());
    		Vector3 lookDir = TransformMatrix.multiplyVector(matCameraRot, cam.getvLookDir());
    		Vector3 vTarget = Vector3.vector_add(cam.getvPosition(), lookDir);
    		TransformMatrix matCamera = TransformMatrix.pointAt(cam.getvPosition(), vTarget, cam.getvUp());
    		
    		TransformMatrix matView = TransformMatrix.Matrix_QuickInverse(matCamera);		
    		
    		
    		for(int j=0;j<mesh.getFaces().length;j++) {
    			Face face = mesh.getFaces()[j];
    			
    			Vector3 a = mesh.getVertices()[face.getA()];
    			Vector3 b = mesh.getVertices()[face.getB()];
    			Vector3 c = mesh.getVertices()[face.getC()];
    			
    			
    			Vector3[]	triProjected = {new Vector3(0,0,0),new Vector3(0,0,0),new Vector3(0,0,0)},
    						triTransformed = {new Vector3(0,0,0),new Vector3(0,0,0),new Vector3(0,0,0)},
    						triViewed = {new Vector3(0,0,0),new Vector3(0,0,0),new Vector3(0,0,0)};
    			
    			
    			triTransformed[0] = Vector3.transform(a, matWorld);
    			triTransformed[1] = Vector3.transform(b, matWorld);
    			triTransformed[2] = Vector3.transform(c, matWorld);
    			
    			
    			Vector3 normal,line1,line2;
    			
    			line1 = Vector3.vector_sub(triTransformed[1], triTransformed[0]);
    			line2 = Vector3.vector_sub(triTransformed[2], triTransformed[0]);
    			
    			normal = Vector3.cross_product(line1, line2);
    			
    			normal = Vector3.normalize(normal);
    			
    			Vector3 vCameraRay = Vector3.vector_sub(triTransformed[0], cam.getvPosition());
    			
    			double dot = Vector3.dot_product(normal, vCameraRay);
    			
    			if(dot <= 0.0) {
    			//if(true) {	
    			
    				triViewed[0] = Vector3.transform(triTransformed[0], matView);
    				triViewed[1] = Vector3.transform(triTransformed[1], matView);
    				triViewed[2] = Vector3.transform(triTransformed[2], matView);
    				
    				triProjected[0] = Vector3.transform(triViewed[0], matProj);
    				triProjected[1] = Vector3.transform(triViewed[1], matProj);
    				triProjected[2] = Vector3.transform(triViewed[2], matProj);
    				
    				triProjected[0] = Vector3.div(triProjected[0],triProjected[0].getW() );
    				triProjected[1] = Vector3.div(triProjected[1],triProjected[1].getW() );
    				triProjected[2] = Vector3.div(triProjected[2],triProjected[2].getW() );
    				
    				triProjected[0].setX( -triProjected[0].getX() );
    				triProjected[1].setX( -triProjected[1].getX() );
    				triProjected[2].setX( -triProjected[2].getX() );
    				
    				triProjected[0].setY( -triProjected[0].getY() );
    				triProjected[1].setY( -triProjected[1].getY() );
    				triProjected[2].setY( -triProjected[2].getY() );
    				
    				
    				Vector3 vOffsetView = new Vector3(1,1,0);
    				
    				triProjected[0] = Vector3.vector_add(triProjected[0], vOffsetView);
    				triProjected[1] = Vector3.vector_add(triProjected[1], vOffsetView);
    				triProjected[2] = Vector3.vector_add(triProjected[2], vOffsetView);
    				
    				triProjected[0].setX(  triProjected[0].getX()*0.5*bmp.getWidth()); 
    				triProjected[0].setY(  triProjected[0].getY()*0.5*bmp.getHeight()); 
    				
    				triProjected[1].setX(  triProjected[1].getX()*0.5*bmp.getWidth()); 
    				triProjected[1].setY(  triProjected[1].getY()*0.5*bmp.getHeight()); 
    				
    				triProjected[2].setX(  triProjected[2].getX()*0.5*bmp.getWidth()); 
    				triProjected[2].setY(  triProjected[2].getY()*0.5*bmp.getHeight()); 
    				
    				
    				drawFilledTriangle(triProjected[0].project(),triProjected[1].project(),triProjected[2].project(),Color.YELLOW);
    				
    			}
    		}
    		
    	}
 		
    	 
     }
     
     private void drawTriangle(Vector2 pointA, Vector2 pointB, Vector2 pointC, Color c) {
    	 Path2D path = this.path(pointA, pointB, pointC);
    	 g2d = (Graphics2D) bmp.getGraphics();
         g2d.setColor(c);
         g2d.draw(path);
	}
     
     private void drawFilledTriangle(Vector2 pointA, Vector2 pointB, Vector2 pointC, Color c) {
    	 Path2D path = this.path(pointA, pointB, pointC);
    	 g2d = (Graphics2D) bmp.getGraphics();
         g2d.setColor(c);
         g2d.fill(path);
         g2d.setColor(Color.RED);
         g2d.draw(path);
	}
     

	public BufferedImage getBufferedImage() {
    	 return bmp;
     }

}
