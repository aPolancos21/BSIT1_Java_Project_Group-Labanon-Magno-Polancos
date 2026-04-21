import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/FirstAidServlet")
public class FirstAidServlet extends HttpServlet {

    private static final String ALL_DATA = """
    [
      {
        "id": 1, "cat": "wounds",
        "title": "Minor Cut or Wound",
        "label": "Wounds & Bleeding",
        "urgency": "low",
        "preview": "Small cuts from sharp objects. Control bleeding and clean the wound to prevent infection.",
        "steps": []
      },
      {
        "id": 2, "cat": "wounds",
        "title": "Severe Bleeding",
        "label": "Wounds & Bleeding",
        "urgency": "high",
        "preview": "Heavy or uncontrolled bleeding from a large wound. Requires immediate action.",
        "steps": []
      },
      {
        "id": 3, "cat": "burns",
        "title": "Minor Burn (1st Degree)",
        "label": "Burns",
        "urgency": "low",
        "preview": "Superficial burn affecting only the outer skin. Caused by brief contact with heat or sun.",
        "steps": []
      },
      {
        "id": 4, "cat": "burns",
        "title": "Severe Burn (2nd/3rd Degree)",
        "label": "Burns",
        "urgency": "high",
        "preview": "Deep burns with blistering or charred skin. Requires immediate medical care.",
        "steps": []
      },
      {
        "id": 5, "cat": "breathing",
        "title": "Choking (Adult)",
        "label": "Breathing",
        "urgency": "high",
        "preview": "Airway blockage causing inability to breathe, speak, or cough.",
        "steps": []
      },
      {
        "id": 6, "cat": "breathing",
        "title": "Choking (Infant)",
        "label": "Breathing",
        "urgency": "high",
        "preview": "Airway blockage in a baby under 1 year old.",
        "steps": []
      },
      {
        "id": 7, "cat": "bone",
        "title": "Suspected Fracture",
        "label": "Bone & Muscle",
        "urgency": "mid",
        "preview": "A possible broken bone after a fall, impact, or accident.",
        "steps": []
      },
      {
        "id": 8, "cat": "bone",
        "title": "Sprain or Strain",
        "label": "Bone & Muscle",
        "urgency": "low",
        "preview": "Twisted ankle or stretched muscle. Use the RICE method.",
        "steps": []
      },
      {
        "id": 9, "cat": "head",
        "title": "Head Injury / Concussion",
        "label": "Head",
        "urgency": "high",
        "preview": "A blow to the head that may cause dizziness, confusion, or loss of consciousness.",
        "steps": []
      },
      {
        "id": 10, "cat": "head",
        "title": "Nosebleed",
        "label": "Head",
        "urgency": "low",
        "preview": "Bleeding from the nose. Usually stops within minutes.",
        "steps": []
      }
    ]
    """;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String search = request.getParameter("search");
        String category = request.getParameter("category");


        PrintWriter out = response.getWriter();
        out.print(ALL_DATA.trim());
        out.flush();
    }
}



