package SCP079.Commands;

import SCP079.App;
import SCP079.Objects.CommandManager;
import SCP079.Objects.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.List;

public class HelpCommand implements ICommand {

    private CommandManager manager;
    private Collection<ICommand> Commands;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
        Commands = manager.getCommands();
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String joined = String.join(" ", args);

        if(!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
            event.getChannel().sendMessage("봇이 임베디드 링크를 보낼 권한이 없습니다.").queue();
            event.getChannel().sendMessage("봇이 링크를 보낼 권한이 제대로 설정되어있는지 확인하여 주시기 바랍니다.\n" +
                    "비밀 채널의 경우, 링크 권한이 / 가 아닌 V 로 체크를 하셔야 합니다.").queue();

            return;
        }

        if (args.isEmpty()) {
            generateAndSendEmbed(event);
            return;
        }


        ICommand command = manager.getCommand(joined);

        if(command == null) {
            event.getChannel().sendMessage( "`"+joined + "`는 존재하지 않는 명령어 입니다.\n" +
                    "`" + App.getPREFIX() + getInvoke() + "` 를 사용해 명령어 리스트를 확인하세요.").queue();
            return;
        }

        String message = "`" + command.getInvoke() + "`에 대한 설명\n" + command.getHelp();

        event.getChannel().sendMessage(message).queue();
    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event) {
        EmbedBuilder builder = EmbedUtils.defaultEmbed().setTitle("명령어 리스트:");

        Commands.forEach(iCommand -> {
            builder.addField(
                    "`" + iCommand.getInvoke() + "`\n",
                    iCommand.getSmallHelp(),
                    false
            );
        });
        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getHelp() {
        return "모르는 명령어는 어디서? 여기서.\n" +
                "명령어: `" + App.getPREFIX() + getInvoke() + " [command]`";
    }

    @Override
    public String getInvoke() {
        return "명령어";
    }

    @Override
    public String getSmallHelp() {
        return "도움말";
    }
}
