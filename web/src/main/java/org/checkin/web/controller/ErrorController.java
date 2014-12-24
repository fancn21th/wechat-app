package org.checkin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-16
 */
@Controller
@RequestMapping("/")
public class ErrorController {
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "error/403";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String notFound() {
        return "error/404";
    }
}
