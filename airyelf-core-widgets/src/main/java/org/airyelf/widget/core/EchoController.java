package org.airyelf.widget.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */
@Controller
public class EchoController {
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    @ResponseBody
    public String register(@RequestParam String text) {
        return text;
    }
}
