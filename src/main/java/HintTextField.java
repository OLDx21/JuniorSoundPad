import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HintTextField extends JTextField {

    Font gainFont = new Font("Tahoma", Font.PLAIN, 13);
    Font lostFont = new Font("Tahoma", Font.ITALIC, 13);

    public HintTextField(final String hint) {

        setText(hint);
        setFont(lostFont);
        setForeground(Color.LIGHT_GRAY);

        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(hint)) {
                    setText("");
                    setFont(gainFont);
                } else {
                    setText(getText());
                    setFont(gainFont);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().equals(hint)|| getText().length()==0) {
                    setText(hint);
                    setFont(lostFont);
                    setForeground(Color.LIGHT_GRAY);
                } else {
                    setText(getText());
                    setFont(gainFont);
                    setForeground(Color.LIGHT_GRAY);
                }
            }
        });

    }
}