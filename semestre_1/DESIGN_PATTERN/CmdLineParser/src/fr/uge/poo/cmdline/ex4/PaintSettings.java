package fr.uge.poo.cmdline.ex4;

import java.net.InetSocketAddress;

public class PaintSettings {
	private final boolean legacy;
    private final boolean bordered;
    private final int borderWidth;
    private final String windowName;
    private final int windowWidthMin;
    private final int windowHeightMin;
    private final InetSocketAddress socketAddress;

    public static class PaintSettingsBuilder {
    	private boolean legacy = false;
        private boolean bordered = false;
        private int borderWidth = 10;
        private String windowName = "";
        private int windowWidthMin = 500;
        private int windowHeightMin = 500;
        private InetSocketAddress socketAddress;
        
        public PaintSettingsBuilder setLegacy(boolean legacy) {
        	this.legacy = legacy;
        	return this;
        }
        
        public PaintSettingsBuilder setBordered(boolean bordered) {
        	this.bordered = bordered;
        	return this;
        }
        
    	public PaintSettingsBuilder setWindowName(String windowName) {
    		this.windowName = windowName;
    		return this;
    	}
    	
    	public PaintSettingsBuilder setBorderWidth(int borderWidth) {
    		this.borderWidth = borderWidth;
    		return this;
    	}
    	
    	public PaintSettingsBuilder setWindowWidthMin(int windowWidthMin) {
    		this.windowWidthMin = windowWidthMin;
    		return this;
    	}
    	
    	public PaintSettingsBuilder setWindowHeightMin(int windowHeightMin) {
    		this.windowHeightMin = windowHeightMin;
    		return this;
    	}
    	
    	public PaintSettingsBuilder setSocketAddress(InetSocketAddress socketAddress) {
    		this.socketAddress = socketAddress;
    		return this;
    	}
    	
    	public PaintSettings build() {
    		if(windowName == null) {
    			throw new IllegalStateException();
    		}
    		return new PaintSettings(this);
    	}
    }
    
    private PaintSettings(PaintSettingsBuilder paintSettingsBuilder) {
    	this.legacy = paintSettingsBuilder.legacy;
    	this.bordered = paintSettingsBuilder.bordered;
    	this.windowName = paintSettingsBuilder.windowName;
    	this.borderWidth = paintSettingsBuilder.borderWidth;
    	this.windowWidthMin = paintSettingsBuilder.windowWidthMin;
    	this.windowHeightMin = paintSettingsBuilder.windowHeightMin;
    	this.socketAddress = paintSettingsBuilder.socketAddress;
    }

    public boolean isLegacy(){
        return legacy;
    }

    public boolean isBordered(){
        return bordered;
    }
    
    public static PaintSettingsBuilder getPaintSettingBuilder() {
    	return new PaintSettingsBuilder();
    }

    @Override
    public String toString(){
        return "PaintSettings [ bordered = " + bordered
				+ ", legacy = " + legacy
				+ ",border width = " + borderWidth
				+ ", window name = "+ windowName +" ]";
    }
}
