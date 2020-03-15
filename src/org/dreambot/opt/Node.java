package org.dreambot.opt;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.opt.CooksAssitant;

public abstract class Node extends MethodProvider {
    protected final CooksAssitant c;

    public Node(CooksAssitant main){
        this.c = main;
    }

    public abstract boolean validate();
    public abstract int execute();

}
