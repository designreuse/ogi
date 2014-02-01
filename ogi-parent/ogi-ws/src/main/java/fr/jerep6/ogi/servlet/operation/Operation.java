package fr.jerep6.ogi.servlet.operation;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Operation {

	public abstract BufferedImage compute(BufferedImage img);
}
