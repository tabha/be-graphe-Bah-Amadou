package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;

public class PerimetreRecherche {
	private Point pointHaut;
	private Point pointBas;
	
	public PerimetreRecherche(Point pointHaut,Point pointBas) {
		this.pointBas = pointBas;
		this.pointHaut = pointHaut;
	}
	
	public PerimetreRecherche(Node originA,Node destinationA,Node originB,Node destinationB) {
		
        float HighLatitude, HighLongitude,LowLatitude,Lowlongitude;
        
        HighLatitude = originA.getPoint().getLatitude();
        HighLatitude = (HighLatitude >= originB.getPoint().getLatitude()) ? HighLatitude: originB.getPoint().getLatitude();
        HighLatitude = (HighLatitude >= destinationA.getPoint().getLatitude()) ? HighLatitude: destinationA.getPoint().getLatitude();
        HighLatitude = (HighLatitude >= destinationB.getPoint().getLatitude()) ? HighLatitude: destinationB.getPoint().getLatitude();
        
        HighLongitude = originA.getPoint().getLongitude();
        HighLongitude = (HighLongitude >= originB.getPoint().getLongitude()) ? HighLongitude: originB.getPoint().getLongitude();
        HighLongitude = (HighLongitude >= destinationA.getPoint().getLongitude()) ? HighLongitude: destinationA.getPoint().getLongitude();
        HighLongitude = (HighLongitude >= destinationB.getPoint().getLongitude()) ? HighLongitude: destinationB.getPoint().getLongitude();
        
        LowLatitude = originA.getPoint().getLatitude();
        LowLatitude = (LowLatitude <= originB.getPoint().getLatitude()) ? LowLatitude: originB.getPoint().getLatitude();
        LowLatitude = (LowLatitude <= destinationA.getPoint().getLatitude()) ? LowLatitude: destinationA.getPoint().getLatitude();
        LowLatitude = (LowLatitude <= destinationB.getPoint().getLatitude()) ? LowLatitude: destinationB.getPoint().getLatitude();
        
        Lowlongitude = originA.getPoint().getLongitude();
        Lowlongitude = (Lowlongitude <= originB.getPoint().getLongitude()) ? Lowlongitude: originB.getPoint().getLongitude();
        Lowlongitude = (Lowlongitude <= destinationA.getPoint().getLongitude()) ? Lowlongitude: destinationA.getPoint().getLongitude();
        Lowlongitude = (Lowlongitude <= destinationB.getPoint().getLongitude()) ? Lowlongitude: destinationB.getPoint().getLongitude();
        Point pointHaut,pointBas;
        pointHaut = new Point(HighLongitude,LowLatitude);
        pointBas = new Point(Lowlongitude,HighLatitude);
        this.pointBas = pointBas;
        this.pointHaut = pointHaut;
	}
	
	public boolean isInPerimetre(Point point) {
		
		return ( point.getLatitude() >= this.pointHaut.getLatitude() &&
				 point.getLatitude() <= this.pointBas.getLatitude()  &&
				 point.getLongitude() >= this.pointBas.getLongitude() &&
				 point.getLongitude() <= this.pointHaut.getLongitude()
				);
	}


}
