package hebe;

import hades.models.PortStdLogic1164;
import hades.models.StdLogic1164;
import hades.signals.Signal;
import hades.simulator.Port;
import hades.simulator.SimEvent1164;
import hades.symbols.BboxRectangle;
import hades.symbols.Label;
import hades.symbols.PortSymbol;
import hades.symbols.Rectangle;
import hades.symbols.Symbol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeronimo
 */
public class OR extends hades.models.rtlib.GenericRtlibObject {

    private Label lbl_A, lbl_B, lbl_Y;
    private String sa, sb, sy;

    protected PortStdLogic1164 port_A, port_B, port_Y;

    public OR() {
        super();
        sa = "_";
        sb = "_";
        sy = "_";
    }

    @Override
    public void constructPorts() {
        port_A = new PortStdLogic1164(this, "A", Port.IN, null);
        port_B = new PortStdLogic1164(this, "B", Port.IN, null);
        port_Y = new PortStdLogic1164(this, "Y", Port.OUT, null);

        ports = new Port[3];
        ports[0] = port_A;
        ports[1] = port_B;
        ports[2] = port_Y;

    }

    public void setText() {
        lbl_A.setText(sa);
        lbl_B.setText(sb);
        lbl_Y.setText(sy);
        getSymbol().painter.paint(getSymbol(), 100);
    }

    @Override
    public void evaluate(Object arg) {
        double time;

        //Execução da porta ou
        //Verificar se as entradas estão ligadas
        if (port_A.getSignal() == null || port_B.getSignal() == null) {
            return;
        }

        //Std1165 = nível 1 = 3, nível 0 = 2 e nível x = 1;
        //se estivere ler as entradas e processar a saída
        Signal signal_A = port_A.getSignal(), signal_B = port_B.getSignal();

        StdLogic1164 d_a = (StdLogic1164) signal_A.getValue(), d_b = (StdLogic1164) signal_B.getValue();

        int a = (int) d_a.getValue(), b = (int) d_b.getValue(), y;

        sa = "A = " + Integer.toString(a - 2);
        sb = "B = " + Integer.toString(b - 2);

        //saida = a == 3 || b == 3
        if (a == 3 || b == 3) {
            y = 3;
        } else {
            y = 2;
        }
        sy = "Y = " + Integer.toString(y - 2);
        setText();
        //verificar se a saída está ligada, se não saia
        if (port_Y.getSignal() != null) {
            Signal signal_Y = port_Y.getSignal();
            StdLogic1164 d_y = new StdLogic1164(y);
            time = simulator.getSimTime() + delay;
            simulator.scheduleEvent(SimEvent1164.createNewSimEvent(signal_Y, time, d_y, port_Y));
        }
    }

    @Override
    public boolean needsDynamicSymbol() {
        return true;
    }

    @Override
    public void constructDynamicSymbol() {
        symbol = new Symbol();
        symbol.setParent(this);

        BboxRectangle bbr = new BboxRectangle();
        bbr.initialize("0 -1800 1200 1800");
        symbol.addMember(bbr);

        Rectangle rec = new Rectangle();
        rec.initialize("0 0 1200 1800");
        symbol.addMember(rec);

        PortSymbol portsymbol;

        portsymbol = new PortSymbol();
        portsymbol.initialize("0 600 " + port_A.getName());
        symbol.addMember(portsymbol);

        portsymbol = new PortSymbol();
        portsymbol.initialize("0 1200 " + port_B.getName());
        symbol.addMember(portsymbol);

        portsymbol = new PortSymbol();
        portsymbol.initialize("1200 600 " + port_Y.getName());
        symbol.addMember(portsymbol);

        Label lbl_nome = new Label();
        lbl_nome.initialize("600 900 2 OR");
        symbol.addMember(lbl_nome);

        lbl_A = new Label();
        lbl_A.initialize("0 -900 1 " + sa);
        symbol.addMember(lbl_A);

        lbl_B = new Label();
        lbl_B.initialize("0 -600 1 " + sb);
        symbol.addMember(lbl_B);

        lbl_Y = new Label();
        lbl_Y.initialize("0 -300 1 " + sy);
        symbol.addMember(lbl_Y);
    }
}
