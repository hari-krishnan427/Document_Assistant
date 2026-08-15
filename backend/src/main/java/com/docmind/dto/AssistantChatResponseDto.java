package com.docmind.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistantChatResponseDto {
    private String response;
    private String intent;
    private String actionType;
    private Map<String, Object> actionData = new HashMap<>();
    private List<String> suggestedPrompts = new ArrayList<>();

    public AssistantChatResponseDto() {}

    public AssistantChatResponseDto(String response, String intent, String actionType, 
                                    Map<String, Object> actionData, List<String> suggestedPrompts) {
        this.response = response;
        this.intent = intent;
        this.actionType = actionType;
        this.actionData = actionData != null ? actionData : new HashMap<>();
        this.suggestedPrompts = suggestedPrompts != null ? suggestedPrompts : new ArrayList<>();
    }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public Map<String, Object> getActionData() { return actionData; }
    public void setActionData(Map<String, Object> actionData) { this.actionData = actionData; }

    public List<String> getSuggestedPrompts() { return suggestedPrompts; }
    public void setSuggestedPrompts(List<String> suggestedPrompts) { this.suggestedPrompts = suggestedPrompts; }

    public static AssistantChatResponseDtoBuilder builder() { return new AssistantChatResponseDtoBuilder(); }

    public static class AssistantChatResponseDtoBuilder {
        private String response;
        private String intent;
        private String actionType;
        private Map<String, Object> actionData = new HashMap<>();
        private List<String> suggestedPrompts = new ArrayList<>();

        public AssistantChatResponseDtoBuilder response(String response) { this.response = response; return this; }
        public AssistantChatResponseDtoBuilder intent(String intent) { this.intent = intent; return this; }
        public AssistantChatResponseDtoBuilder actionType(String actionType) { this.actionType = actionType; return this; }
        public AssistantChatResponseDtoBuilder actionData(Map<String, Object> actionData) { this.actionData = actionData; return this; }
        public AssistantChatResponseDtoBuilder suggestedPrompts(List<String> suggestedPrompts) { this.suggestedPrompts = suggestedPrompts; return this; }

        public AssistantChatResponseDto build() {
            return new AssistantChatResponseDto(response, intent, actionType, actionData, suggestedPrompts);
        }
    }
}
