import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/FirstAidServlet")
public class FirstAidServlet extends HttpServlet {
    private static final Pattern ITEM_PATTERN = Pattern.compile("(?s)\\{.*?\\n\\s*\\}");
    private static final Pattern JSON_VALUE_PATTERN =
            Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");

    private static final String ALL_DATA = """
    [
      {
        "id": 1, "cat": "wounds",
        "title": "Minor Cut or Wound",
        "label": "Wounds & Bleeding",
        "urgency": "low",
        "preview": "Small cuts from sharp objects. Control bleeding and clean the wound to prevent infection.",
        "steps": [
          "Wash your hands thoroughly with soap and water before touching the wound.",
          "Apply gentle pressure with a clean cloth or gauze for 5-10 minutes to stop bleeding.",
          "Rinse the wound under clean running water for 1-2 minutes.",
          "Clean around the wound with mild soap - avoid putting soap directly inside the cut.",
          "Apply a thin layer of antibiotic ointment (e.g. Betadine) to prevent infection.",
          "Cover with a sterile bandage. Change daily or when wet.",
          "Watch for infection signs: redness, swelling, warmth, or pus."
        ]
      },
      {
        "id": 2, "cat": "wounds",
        "title": "Severe Bleeding",
        "label": "Wounds & Bleeding",
        "urgency": "high",
        "preview": "Heavy or uncontrolled bleeding from a large wound. Requires immediate action.",
        "steps": [
          "Call emergency services (911 or 8-911) immediately.",
          "Put on gloves or use a plastic bag to protect yourself.",
          "Apply firm, direct pressure to the wound using a clean cloth or gauze.",
          "Do NOT remove cloth if soaked - add more layers on top and keep pressing.",
          "If trained, apply a tourniquet 2-3 inches above the wound for limb injuries.",
          "Lay the person down. Elevate legs slightly unless head/spine injury is suspected.",
          "Keep the person calm and warm until help arrives."
        ]
      },
      {
        "id": 3, "cat": "burns",
        "title": "Minor Burn (1st Degree)",
        "label": "Burns",
        "urgency": "low",
        "preview": "Superficial burn affecting only the outer skin. Caused by brief contact with heat or sun.",
        "steps": [
          "Remove person from heat source.",
          "Cool under cool running water for 10-20 minutes.",
          "Remove jewelry near the burn before swelling starts.",
          "Do not pop blisters.",
          "Apply sterile non-stick bandage loosely.",
          "Take paracetamol (Biogesic) for pain if needed.",
          "Keep clean and watch for signs of infection."
        ]
      },
      {
        "id": 4, "cat": "burns",
        "title": "Severe Burn (2nd/3rd Degree)",
        "label": "Burns",
        "urgency": "high",
        "preview": "Deep burns with blistering or charred skin. Requires immediate medical care.",
        "steps": [
          "Call emergency services (911) immediately.",
          "Move person away from heat. Ensure your own safety.",
          "Do NOT pour water on large/severe burns.",
          "Do NOT remove clothing stuck to burned skin.",
          "Cover burn loosely with clean dry material or cling wrap.",
          "Do not apply creams, butter, or ice.",
          "Keep warm and monitor breathing until help arrives."
        ]
      },
      {
        "id": 5, "cat": "breathing",
        "title": "Choking (Adult)",
        "label": "Breathing",
        "urgency": "high",
        "preview": "Airway blockage causing inability to breathe, speak, or cough.",
        "steps": [
          "Ask: Are you choking? If they cannot speak or breathe, act immediately.",
          "Lean them forward. Give 5 firm back blows between shoulder blades.",
          "If back blows fail, perform abdominal thrusts (Heimlich maneuver).",
          "Stand behind them. Make a fist above navel, grasp with other hand.",
          "Give 5 quick upward-inward thrusts.",
          "Alternate 5 back blows and 5 abdominal thrusts.",
          "If unconscious, lower to ground, begin CPR, and call 911."
        ]
      },
      {
        "id": 6, "cat": "breathing",
        "title": "Choking (Infant)",
        "label": "Breathing",
        "urgency": "high",
        "preview": "Airway blockage in a baby under 1 year old.",
        "steps": [
          "Call 911 immediately.",
          "Hold baby face-down on forearm, head lower than chest.",
          "Give 5 firm back blows between shoulder blades.",
          "Turn face-up. Give 5 chest thrusts with 2 fingers below nipple line.",
          "Alternate 5 back blows and 5 chest thrusts.",
          "Remove object only if clearly visible in mouth.",
          "Begin infant CPR if baby stops breathing."
        ]
      },
      {
        "id": 7, "cat": "bone",
        "title": "Suspected Fracture",
        "label": "Bone & Muscle",
        "urgency": "mid",
        "preview": "A possible broken bone after a fall, impact, or accident.",
        "steps": [
          "Keep person still. Do not move unless in immediate danger.",
          "Immobilize the injured area. Do not try to align the bone.",
          "Apply a splint using a padded stiff item above and below the break.",
          "Apply ice pack wrapped in cloth for 20 minutes at a time.",
          "Elevate limb if possible without causing pain.",
          "Watch for shock: pale skin, rapid breathing, weakness.",
          "Get to an emergency room as soon as possible."
        ]
      },
      {
        "id": 8, "cat": "bone",
        "title": "Sprain or Strain",
        "label": "Bone & Muscle",
        "urgency": "low",
        "preview": "Twisted ankle or stretched muscle. Use the RICE method.",
        "steps": [
          "R - Rest: Stop the activity immediately.",
          "I - Ice: Apply ice pack (wrapped) for 15-20 min every 2-3 hrs for 48 hrs.",
          "C - Compression: Wrap with elastic bandage. Not too tight.",
          "E - Elevation: Raise limb above heart level.",
          "Take paracetamol or ibuprofen for pain if needed.",
          "Avoid heat, massage, or alcohol for first 72 hours.",
          "Gradually return to activity after 2-3 days."
        ]
      },
      {
        "id": 9, "cat": "head",
        "title": "Head Injury / Concussion",
        "label": "Head",
        "urgency": "high",
        "preview": "A blow to the head that may cause dizziness, confusion, or loss of consciousness.",
        "steps": [
          "Check if person is conscious. If unconscious, call 911.",
          "Keep still. Do not move if spinal injury is possible.",
          "Apply cloth gently to scalp bleeding. No pressure if skull fracture suspected.",
          "Monitor: vomiting, confusion, seizures, unequal pupils.",
          "Do NOT give aspirin or ibuprofen.",
          "Let them rest but check every 30 minutes.",
          "Drive to ER or call 911 if they seem drowsy or confused."
        ]
      },
      {
        "id": 10, "cat": "head",
        "title": "Nosebleed",
        "label": "Head",
        "urgency": "low",
        "preview": "Bleeding from the nose. Usually stops within minutes.",
        "steps": [
          "Sit upright and lean slightly forward.",
          "Pinch the soft part of the nose firmly.",
          "Breathe through mouth and hold for 10-15 minutes.",
          "Apply cold compress to bridge of nose.",
          "Do not stuff tissues deep into nostril.",
          "After stopping, avoid blowing nose for several hours.",
          "Avoid strenuous activity or hot drinks for the rest of the day."
        ]
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
        out.print(buildResponse(search, category));
        out.flush();
    }

    private static String buildResponse(String search, String category) {
        String normalizedSearch = normalize(search);
        String normalizedCategory = normalize(category);

        if (normalizedSearch.isEmpty() && (normalizedCategory.isEmpty() || "all".equals(normalizedCategory))) {
            return ALL_DATA.trim();
        }

        List<String> matches = new ArrayList<>();
        Matcher matcher = ITEM_PATTERN.matcher(ALL_DATA);

        while (matcher.find()) {
            String item = matcher.group();
            if (matchesCategory(item, normalizedCategory) && matchesSearch(item, normalizedSearch)) {
                matches.add(item);
            }
        }

        if (matches.isEmpty()) {
            return "[]";
        }

        return "[\n" + String.join(",\n", matches) + "\n]";
    }

    private static boolean matchesCategory(String item, String category) {
        if (category.isEmpty() || "all".equals(category)) {
            return true;
        }

        return category.equals(normalize(extractValue(item, "cat")));
    }

    private static boolean matchesSearch(String item, String search) {
        if (search.isEmpty()) {
            return true;
        }

        return normalize(extractValue(item, "title")).contains(search)
                || normalize(extractValue(item, "label")).contains(search)
                || normalize(extractValue(item, "preview")).contains(search)
                || normalize(item).contains(search);
    }

    private static String extractValue(String item, String key) {
        Matcher matcher = JSON_VALUE_PATTERN.matcher(item);
        while (matcher.find()) {
            if (key.equals(matcher.group(1))) {
                return matcher.group(2);
            }
        }
        return "";
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
