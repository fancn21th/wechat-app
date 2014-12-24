package org.checkin.web.controller;

import org.checkin.web.vo.TicketVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Verify ticket.
 * <p/>
 * No crypt default.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-23
 */
@Controller
@RequestMapping("/ticket")
public class VerifyTicketController {
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String getTicket(@RequestBody TicketVo ticket, HttpServletRequest request) {
        if (ticket != null && "component_verify_ticket".equals(ticket.getInfoType())) {
            request.getSession().getServletContext()
                    .setAttribute("component_verify_ticket", ticket.getComponentVerifyTicket());
        }
        return "sucess";
    }
}
