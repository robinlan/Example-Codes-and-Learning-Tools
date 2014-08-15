package com.bn.gjxq;
import java.util.Set;
public class Normal 
{
   public static final float DIFF=0.0000001f;
   float nx;
   float ny;
   float nz; 
   public Normal(float nx,float ny,float nz)
   {
	   this.nx=nx;
	   this.ny=ny;
	   this.nz=nz;
   }
   @Override 
   public boolean equals(Object o)
   {
	   if(o instanceof  Normal)
	   {
		   Normal tn=(Normal)o;
		   if(Math.abs(nx-tn.nx)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF&&
			  Math.abs(ny-tn.ny)<DIFF
             )
		   {
			   return true;
		   }
		   else
		   {
			   return false;
		   }
	   }
	   else
	   {
		   return false;
	   }
   }
   @Override
   public int hashCode()
   {
	   return 1;
   }
   public static float[] getAverage(Set<Normal> sn)
   {
	   float[] result=new float[3];
	   for(Normal n:sn)
	   {
		   result[0]+=n.nx;
		   result[1]+=n.ny;
		   result[2]+=n.nz;
	   }	   
	   return LoadUtil.vectorNormal(result);
   }
}
