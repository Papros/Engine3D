package Model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.Vector;

public class Device {

	BufferedImage bmp;
	
	Graphics2D g2d;
	TransformMatrix viewMatrix;
	Camera cam;
	Mesh[] meshList;
	
	Vector<IndependFace> facesToDraw;
	
	public Device() {
		cam = new Camera();
		meshList = new Mesh[1];
		meshList[0] = new Mesh();
		facesToDraw = new Vector<IndependFace>();
	}
	
	public Device(Camera c, Mesh[] m ) {
		cam = c;
		meshList = m;
		facesToDraw = new Vector<IndependFace>();
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
     
     public Vector3 inteserectPlane(Vector3 plane_p, Vector3 plane_n, Vector3 lineStart, Vector3 lineEnd) {
    	 plane_n = Vector3.normalize(plane_n);
    	 double plane_d = -Vector3.dot_product(plane_n, plane_p);
    	 double ad = Vector3.dot_product(lineStart, plane_n);
    	 double bd = Vector3.dot_product(lineEnd, plane_n);
    	 double t = (-plane_d - ad) / (bd - ad);
    	 Vector3 lineStartToEnd = Vector3.vector_sub(lineEnd, lineStart);
    	 Vector3 lineToIntersect = Vector3.multiply(lineStartToEnd, t);
    	 return Vector3.vector_add(lineStart, lineToIntersect);
    	 
     }
     
     public double dist(Vector3 n,Vector3 f,Vector3 p) {
    	 Vector3 n1 = Vector3.normalize(p);
    	 return( n.getX()*p.getX() + n.getY()*p.getY() + n.getZ()+p.getZ() - Vector3.dot_product(n, f));
    	// return( n.getX()*n.getX() + n.getY()*n.getY() + n.getZ()+n.getZ() - Vector3.dot_product(n, f));
     }
     
     public IndependFace[] clipAgainsPlane(Vector3 plane_p, Vector3 plane_n,IndependFace in_tri) {
    	 IndependFace f1,f2;
    	 IndependFace[] cutF;
    	 
    	 plane_n = Vector3.normalize(plane_n);
    	 
    	 Vector3 insidePoints[] = new Vector3[3]; int ninsideP = 0;
    	 Vector3 outsidePoints[] = new Vector3[3]; int noutsideP = 0;
    	 
    	 double d0 = dist(plane_n, plane_p, in_tri.getA());
    	 double d1 = dist(plane_n, plane_p, in_tri.getB());
    	 double d2 = dist(plane_n, plane_p, in_tri.getC());
    	 
    	 if( d0 >= 0 ) { insidePoints[ninsideP++] = in_tri.getA(); } 
    	 else { outsidePoints[noutsideP++] = in_tri.getA(); }
    	 
    	 if( d1 >= 0 ) { insidePoints[ninsideP++] = in_tri.getB(); } 
    	 else { outsidePoints[noutsideP++] = in_tri.getB(); }
    	 
    	 if( d2 >= 0 ) { insidePoints[ninsideP++] = in_tri.getC(); } 
    	 else { outsidePoints[noutsideP++] = in_tri.getC(); }
    	 
    	 
    	 if( ninsideP == 0) {
    		 cutF = null;
    	 }
    	 else if( ninsideP == 3) {
    		 cutF = new IndependFace[1];
    		 cutF[0] = in_tri;
    	 }
    	 else if(ninsideP ==1 && noutsideP ==2) {
    		 cutF = new IndependFace[1];
    		 
    		 cutF[0] = new IndependFace();
    		 cutF[0].setA(insidePoints[0]);
    		 cutF[0].setB( inteserectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]) );
    		 cutF[0].setC( inteserectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[1]) );
    	 }
    	 else if(ninsideP == 2 && noutsideP == 1) {
    		 
    		 cutF = new IndependFace[2];
    		 cutF[0] = new IndependFace();
    		 cutF[0].setA(insidePoints[0]);
    		 cutF[0].setB(insidePoints[1]);
    		 cutF[0].setC(  inteserectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]) );
    		 
    		 cutF[1] = new IndependFace();
    		 cutF[1].setA(insidePoints[1]);
    		 cutF[1].setB( cutF[0].getC() );
    		 cutF[1].setC(  inteserectPlane(plane_p, plane_n, insidePoints[1], outsidePoints[0]) );
    		 
    	 }else {
    		 cutF = null;
    	 }
    	 
    	 
    	 return cutF;
     }
     
        
     public void render(Camera cam, Mesh[] meshes) {
    	 
    	Clear(Color.BLACK);
    	
    	TransformMatrix matProj = TransformMatrix.makeProjection(cam.getAngle(),( 9.0 / 16.0), cam.getdNear(), cam.getdFar());
    	
    	
    	for(int i=0;i<meshes.length;i++) {
    		
    		Mesh mesh = meshes[i];
    		
    		TransformMatrix matRotZ,matRotX,matRotY;
    		
    		matRotZ = TransformMatrix.makeRotationZ(mesh.getRotation().getZ());
    		matRotX = TransformMatrix.makeRotationX(mesh.getRotation().getX());
    		matRotY = TransformMatrix.makeRotationY(mesh.getRotation().getY());
    		
    		TransformMatrix matTrans;
    		matTrans = TransformMatrix.makeTranslation(mesh.getPosition().getX(),mesh.getPosition().getY(),mesh.getPosition().getZ());
    		
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
    			
    			if(dot < 0.0) {	
    			
    				triViewed[0] = Vector3.transform(triTransformed[0], matView);
    				triViewed[1] = Vector3.transform(triTransformed[1], matView);
    				triViewed[2] = Vector3.transform(triTransformed[2], matView);
    				
    				IndependFace[] clipped = clipAgainsPlane(new Vector3(0,0,cam.getdNear()), new Vector3(0,0,1),new IndependFace(triViewed[0],triViewed[1],triViewed[2]) ); 
    				
    				if(clipped != null)
    				for(int n=0;n<clipped.length;n++) {
    					
        				triProjected[0] = Vector3.transform(clipped[n].getA(), matProj);
        				triProjected[1] = Vector3.transform(clipped[n].getB(), matProj);
        				triProjected[2] = Vector3.transform(clipped[n].getC(), matProj);
        				
        				
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
        				
        				triProjected[0].print();
        				
        				facesToDraw.add(new IndependFace(triProjected[0],triProjected[1],triProjected[2]));
        				//drawFilledTriangle(triProjected[0].project(),triProjected[1].project(),triProjected[2].project(),Color.YELLOW);
        				
        				
    				}
    				
    			}
    		}
    		
    		
    		
    	}
    	
    	facesToDraw.removeIf(o1 -> ((o1.getA().getZ()+o1.getB().getZ()+o1.getC().getZ()) / 3.0) <= cam.getdNear() );
    	
    	Collections.sort(facesToDraw, new Comparator<IndependFace>() {

			@Override
			public int compare(IndependFace o1, IndependFace o2) {
				// TODO Auto-generated method stub
				double z1 = (o1.getA().getZ()+o1.getB().getZ()+o1.getC().getZ()) / 3.0;
				double z2 = (o2.getA().getZ()+o2.getB().getZ()+o2.getC().getZ()) / 3.0;
			
				if(z1 < z2)  return 1; else 
				if(z1 == z2) return 0; else return -1;
			}
    		
    	});
    	
    	
    	
    	
    	
    	for(IndependFace face: facesToDraw){
    		
    		ArrayDeque<IndependFace> toClip = new ArrayDeque<IndependFace>();
    		
    		toClip.push(face);
    		int nNew = 1;
    		
    		for(int i=0; i <4; i++) {
    			
    			IndependFace[] toAdd = new IndependFace[2];
    			while(nNew > 0) {
    				
    				IndependFace test = toClip.pop();
    				nNew--;
    				
    				switch(i) {
    				case 0: toAdd = clipAgainsPlane( new Vector3(0,0,0), new Vector3(0,1,0), test); break;
    				case 1: toAdd = clipAgainsPlane( new Vector3(0,bmp.getHeight()-1.0,0), new Vector3(0,-1,0), test); break;
    				case 2: toAdd = clipAgainsPlane( new Vector3(0,0,0), new Vector3(1,0,0), test); break;
    				case 3: toAdd = clipAgainsPlane( new Vector3(bmp.getWidth()-1.0,0,0), new Vector3(-1,0,0), test); break;
    				}
    				
    				if(toAdd != null)
    				for(int w = 0; w < toAdd.length; w++) {
    					toClip.push(toAdd[w]);
    				}
    				
    			}
    			
    			nNew = toClip.size();
    		}
    		
    		for(IndependFace f: toClip) {
    			drawFilledTriangle(f.getA().project(),f.getB().project(),f.getC().project(),Color.WHITE);
    		}

    	}
    	
    	
 		facesToDraw.clear();
    	 
 		
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
